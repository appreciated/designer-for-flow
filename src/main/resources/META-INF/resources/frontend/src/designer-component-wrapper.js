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
            slot.no-pointer-events::slotted(*) {
                pointer-events: none;
            }
            #wrapper::after {
                border: 0px solid var(--lumo-primary-color-50pct);
                content: "";
                opacity: 0;
                position: absolute;
                width: 100%;
                height: 100%;
                border-radius: var(--lumo-border-radius);
                transition: border 0.3s ease, opacity 0.3s ease, box-shadow 0.3s ease;
                box-shadow: var(--design-focus-shadow);
                pointer-events: none;
            }
            #wrapper.focus::after {
                opacity: 1;
                box-shadow: var(--design-focus-shadow);
            }
        </style>
        <div id="wrapper" style="position: relative;display: flex;">
            <slot id="content"></slot>
        </div>`;
    }

    ready() {
        var polymerThis = this;
        super.ready();
        this.root.addEventListener('click', function (ev) {
            ev.stopPropagation();
            polymerThis.componentClicked();
        });

        var slot = this.shadowRoot.querySelectorAll('slot')[0];
        var nodes = slot.assignedNodes();
        var firstChild = nodes[0];
        if (firstChild.style["width"]) {
            this.style["width"] = firstChild.style["width"];
        }
        if (firstChild.style["height"]) {
            this.style["height"] = firstChild.style["height"];
        }
        var observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                if (firstChild.style["width"] && firstChild.style["width"].endsWith("%")) {
                    polymerThis.style["width"] = firstChild.style["width"];
                }
                if (firstChild.style["height"] && firstChild.style["height"].endsWith("%")) {
                    polymerThis.style["height"] = firstChild.style["height"];
                }
            });
        });
        observer.observe(firstChild, {
            attributes: true,
            attributeFilter: ["style"]
        });
    }

    componentClicked() {
    }
}

customElements.define(DesignerComponentWrapper.is, DesignerComponentWrapper);