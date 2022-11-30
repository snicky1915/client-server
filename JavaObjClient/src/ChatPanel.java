import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatPanel extends JPanel {
	private static final long serialVersionUID = 381649284153267275L;

	JLabel timeLabel;
	JTextArea chatTextArea;
	int border = 8;

	public ChatPanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

		timeLabel = new JLabel(LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a")));
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeLabel.setAlignmentY(1.0f);
		timeLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));

		chatTextArea = new JTextArea();
		chatTextArea.setAlignmentY(1.0f);
		chatTextArea.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 14));
		chatTextArea.setOpaque(true);
		chatTextArea.setMinimumSize(new Dimension(8, 8));
		chatTextArea.setMaximumSize(new Dimension(240, 1024));
		chatTextArea.setLineWrap(true);
		chatTextArea.setWrapStyleWord(true);
		chatTextArea.setEditable(false);
	}

	public void setText(String text) {
		FontMetrics fontMetrics = chatTextArea.getFontMetrics(chatTextArea.getFont());
		int maxStringWidth = 0;
		for (String s : text.split("\n")) {
			if (maxStringWidth < fontMetrics.stringWidth(s)) {
				maxStringWidth = fontMetrics.stringWidth(s);
			}
		}
		chatTextArea.setMaximumSize(new Dimension(maxStringWidth + border * 2 + 1, Integer.MAX_VALUE));
		chatTextArea.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
		chatTextArea.setText(text);
	}
}
