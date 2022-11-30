import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;

public class ServerChatPanel extends ChatPanel {
	private static final long serialVersionUID = 1L;

	public ServerChatPanel() {
		border = 4;
		
		chatTextArea.setForeground(Color.WHITE);
		chatTextArea.setBackground(Color.LIGHT_GRAY);
		chatTextArea.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		add(chatTextArea);
	}
}
