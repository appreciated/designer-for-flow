import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';

class DesignerComponentWrapper extends PolymerElement {
    static get is() {
        return 'designer-component-wrapper'
    }

    static get template() {
        return html`
        <style>
            ::slotted(*) {
               flex: 1 1;
            }
            #wrapper::after {
                border: 0px solid rgba(255,255,255,0);
                content: "";
                opacity: 0;
                position: absolute;
                width: 100%;
                height: 100%;
                transition: border 0.3s ease;
                pointer-events: none;
            }
            #wrapper.no-pointer-events::after {
                pointer-events: all;
            }
            #wrapper.focus::after {
                opacity: 1;
                box-sizing: border-box;
                border: 1px dashed var(--lumo-primary-color);
            }
        </style>
        <div id="wrapper" style="position: relative;display: flex;">
            <slot></slot>
        </div>`;
    }

    ready() {
        var polymerThis = this;
        super.ready();
        this.root.addEventListener('click', function (ev) {
            ev.stopPropagation();
            polymerThis.componentClicked();
        });
    }

    attached() {
        var slot = this.shadowRoot.querySelectorAll('slot')[0];
        var nodes = slot.assignedNodes();
        this.firstChild = nodes[0];
        if (this.firstChild.style["width"]) {
            this.style["width"] = this.firstChild.style["width"];
        }
        if (this.firstChild.style["height"]) {
            this.style["height"] = this.firstChild.style["height"];
        }
        if (this.mutationObserver == null) {
            this.mutationObserver = new MutationObserver(function (mutations) {
                mutations.forEach(function (mutation) {
                    if (firstChild.style["width"] && firstChild.style["width"].endsWith("%")) {
                        polymerThis.style["width"] = firstChild.style["width"];
                    }
                    if (firstChild.style["height"] && firstChild.style["height"].endsWith("%")) {
                        polymerThis.style["height"] = firstChild.style["height"];
                    }
                });
            });
            this.mutationObserver.observe(firstChild, {
                attributes: true,
                attributeFilter: ["style"]
            });
        }
    }

    componentClicked() {
    }
}

customElements.define(DesignerComponentWrapper.is, DesignerComponentWrapper);