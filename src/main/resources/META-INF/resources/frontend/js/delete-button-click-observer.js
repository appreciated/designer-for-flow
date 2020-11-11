window.initDeleteButtonClickObserver = function initDeleteButtonClickObserver(element) {
    window.deleteButtonClickObserverElement = element.$server;
    window.deleteButtonClickObserver = function deleteButtonClickObserver() {
        window.deleteButtonClickObserverElement.deleteButtonClickEvent();
    }
}