package pennphoto.client.view;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class BaseDialog extends DialogBox {

	private HTML close;
	private HorizontalPanel captionPanel;
	
	public BaseDialog() {
		super(false, true);
		close = new HTML("[X]");
		DOM.setStyleAttribute(close.getElement(), "cursor", "pointer");
		captionPanel = new HorizontalPanel();
		captionPanel.setWidth("100%");
		captionPanel.add(new HTML("&nbsp;"));
		super.setHTML("&nbsp;");
		captionPanel.add(close);
		captionPanel.setCellHorizontalAlignment(close, HorizontalPanel.ALIGN_RIGHT);
		captionPanel.setCellWidth(close, "1%");
		captionPanel.addStyleName("Caption");
		Element td = this.getCellElement(0, 1);
		td.setInnerHTML("");
		td.appendChild(captionPanel.getElement());
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		EventTarget target = event.getEventTarget();
		if (!Element.is(target)) {
			super.onBrowserEvent(event);
			return;
		}
		if (!close.getElement().isOrHasChild(Element.as(target))) {
			super.onBrowserEvent(event);
			return;
		}
		if (event.getTypeInt() == Event.ONMOUSEDOWN) {
			this.hide();
			return;
		}
		super.onBrowserEvent(event);
	}
}
