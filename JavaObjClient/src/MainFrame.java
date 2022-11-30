import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BaseMultiResolutionImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Box;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -8700593713295177769L;
	private JPanel friendsPanel;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(360, 640);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout(0, 0));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
		contentPane.add(leftPanel, BorderLayout.WEST);

		ImageIcon personIcon = new ImageIcon("src/person-fill.png");
		BaseMultiResolutionImage personImage = new BaseMultiResolutionImage(
				personIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH),
				personIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

		ImageIcon chatIcon = new ImageIcon("src/chat-fill.png");
		BaseMultiResolutionImage chatImage = new BaseMultiResolutionImage(
				chatIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH),
				chatIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

		JPanel iconPanel = new JPanel();
		iconPanel.setLayout(new BorderLayout(22, 16));
		leftPanel.add(iconPanel, BorderLayout.NORTH);

		JLabel friendsButton = new JLabel(new ImageIcon(personImage));
		friendsButton.setVerticalAlignment(SwingConstants.TOP);
		friendsButton.setHorizontalAlignment(SwingConstants.LEADING);
		friendsButton.setBorder(BorderFactory.createEmptyBorder());
		friendsButton.setPreferredSize(new Dimension(24, 20));
		iconPanel.add(friendsButton, BorderLayout.NORTH);

		JLabel chatButton = new JLabel(new ImageIcon(chatImage));
		chatButton.setVerticalAlignment(SwingConstants.TOP);
		chatButton.setHorizontalAlignment(SwingConstants.LEADING);
		chatButton.setBorder(BorderFactory.createEmptyBorder());
		chatButton.setPreferredSize(new Dimension(24, 20));
		iconPanel.add(chatButton, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBackground(Color.WHITE);

		friendsPanel = new JPanel();
		panel.add(friendsPanel, BorderLayout.NORTH);
		friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.PAGE_AXIS));
		friendsPanel.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		contentPane.add(scrollPane, BorderLayout.CENTER);

		setVisible(true);
	}

	public void addFriend(String name) {
		friendsPanel.add(new FriendPanel(name), 0);
		friendsPanel.revalidate();
		friendsPanel.repaint();
	}

	public void removeFriend(String name) {
		for (Component component : friendsPanel.getComponents()) {
			FriendPanel friendPanel = (FriendPanel) component;

			if (friendPanel.getName().equals(name)) {
				friendsPanel.remove(component);
				friendsPanel.revalidate();
				friendsPanel.repaint();
				break;
			}
		}
	}
}
