import java.awt.Color;

import javax.swing.BorderFactory;

public class MyChatPanel extends ChatPanel {
	private static final long serialVersionUID = 8726705535149335619L;

	public MyChatPanel() {
		timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		add(timeLabel);

		chatTextArea.setForeground(Color.WHITE);
		chatTextArea.setBackground(Color.BLACK);
		add(chatTextArea);
	}
}
