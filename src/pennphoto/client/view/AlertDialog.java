package pennphoto.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AlertDialog extends DialogBox {
	
	private HTML messageBox;
	public AlertDialog() {
		super(false, true);
		this.setText("Alert");
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("300px", "100px");
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		panel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		panel.setSpacing(10);
		messageBox = new HTML();
		panel.add(messageBox);
		Button ok = new Button("OK");
		panel.add(ok);
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		this.setWidget(panel);
	}
	
	public void setMessage(String message) {
		messageBox.setText(message);
	}
}
