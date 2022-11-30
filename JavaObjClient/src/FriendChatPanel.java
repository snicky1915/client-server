import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FriendChatPanel extends ChatPanel {
	private static final long serialVersionUID = 3705176933832178919L;

	public FriendChatPanel(String name) {
		JLabel ImageLabel = new JLabel();
		ImageLabel.setAlignmentY(0.0f);
		ImageLabel.setMinimumSize(new Dimension(32, 32));
		ImageLabel.setPreferredSize(new Dimension(32, 32));
		ImageLabel.setMaximumSize(new Dimension(32, 32));
		ImageLabel.setBackground(Color.LIGHT_GRAY);
		ImageLabel.setOpaque(true);
		add(ImageLabel);

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		infoPanel.setAlignmentY(0.0f);
		infoPanel.setAlignmentX(0.0f);
		infoPanel.setLayout(new BorderLayout(0, 0));
		add(infoPanel);

		JLabel nameLabel = new JLabel(name);
		nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		nameLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		infoPanel.add(nameLabel, BorderLayout.NORTH);

		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.X_AXIS));
		infoPanel.add(chatPanel, BorderLayout.CENTER);

		chatTextArea.setBackground(Color.WHITE);
		chatPanel.add(chatTextArea);

		timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		chatPanel.add(timeLabel);
	}
}
