const {
    app, session, protocol, BrowserWindow, Menu, globalShortcut
} = require('electron');

const remote = require('electron').remote;

const path = require('path');
let mainWindow = null;
let loading = null;
let serverProcess = null;
const gotTheLock = app.requestSingleInstanceLock();

if (!gotTheLock) {
    app.quit()
} else {
    app.on('second-instance', (event, commandLine, workingDirectory) => {
            if (mainWindow) {
                if (mainWindow.isMinimized()) {
                    mainWindow.restore();
                }
                mainWindow.focus();
            }
        }
    )

    var fs = require("fs");
    var files = fs.readdirSync("./resources/app/java/");
    var filename = null;
    for (var i in files) {
        if (path.extname(files[i]) === ".jar") {
            filename = path.basename(files[i]);
            break;
        }
    }
    if (!filename) {
        throw new Error("There is no JAR file in ./java/ !");
    }
    // Provide API for web application
    global.callElectronUiApi = function (args) {
        console.log('Electron called from web app with args "' + args + '"');
        if (args) {
            if (args[0] === 'exit') {
                console.log('Kill server process');
                const kill = require('tree-kill');
                kill(serverProcess.pid, 'SIGTERM', function (err) {
                    console.log('Server process killed');
                    serverProcess = null;
                    mainWindow.close();
                });
            }
            if (args[0] === 'minimize') {
                mainWindow.minimize();
            }
            if (args[0] === 'maximize') {
                if (!mainWindow.isMaximized()) {
                    mainWindow.maximize();
                } else {
                    mainWindow.unmaximize();
                }
            }
        }
    };

    app.on('window-all-closed', function () {
        app.quit();
    });
    app.on('ready', function () {
        loading = new BrowserWindow({
            show: false
            , frame: false
            , width: 500
            , height: 280
        });
        loading.loadURL(app.getAppPath() + '/loading.html');
        loading.once('ready-to-show', () => {
            loading.show();
        })
        platform = process.platform;
        if (platform === 'win32') {
            serverProcess = require('child_process').spawn('java.exe', ['-jar', filename, '--logging.file=flow-designer.log'], {
                cwd: './resources/app/java/'
            });
        } else if (platform === 'darwin') {
            serverProcess = require('child_process').spawn(app.getAppPath() + './resources/app/java/');
        }
        serverProcess.stdout.on('data', function (data) {
            console.log('Server: ' + data);
        });
        console.log("Server PID: " + serverProcess.pid);
        let appUrl = 'http://localhost:8080';

        const openWindow = function () {
            mainWindow = new BrowserWindow({
                title: 'Flow Designer'
                , show: false
                , width: 1200
                , height: 800
                , frame: true
            });
            mainWindow.setMenu(null);
            mainWindow.loadURL(appUrl);
            mainWindow.once('ready-to-show', () => {
                loading.hide();
                mainWindow.show();
            })
            ;
            mainWindow.on('closed', function () {
                mainWindow = null;
                app.quit();
            });
            mainWindow.on('close', function (e) {
                if (serverProcess) {
                    var choice = require('electron').dialog.showMessageBox(this, {
                        type: 'question'
                        , buttons: ['Yes', 'No']
                        , title: 'Confirm'
                        , message: 'Are you sure you want to quit?'
                    });
                    if (choice == 1) {
                        e.preventDefault();
                    }
                }
            });
        };
        const startUp = function () {
            const requestPromise = require('minimal-request-promise');
            requestPromise.get(appUrl).then(function (response) {
                console.log('Server started!');
                openWindow();
            }, function (response) {
                console.log('Waiting for the server start...');
                setTimeout(function () {
                    startUp();
                }, 200);
            });
        };
        startUp();
    });
}