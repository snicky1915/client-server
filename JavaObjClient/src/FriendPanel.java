import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FriendPanel extends JPanel {
	private static final long serialVersionUID = 3705176933832178919L;
	private String name;

	public FriendPanel(String name) {
		this.name = name;
		
		setBackground(Color.WHITE);
		setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));

		JLabel ImageLabel = new JLabel();
		ImageLabel.setPreferredSize(new Dimension(32, 32));
		ImageLabel.setBackground(Color.LIGHT_GRAY);
		ImageLabel.setOpaque(true);
		add(ImageLabel);

		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 14));
		add(nameLabel);
	}
	
	public String getName() {
		return name;
	}
}
