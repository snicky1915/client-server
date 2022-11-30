import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BaseMultiResolutionImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class JavaObjClientView extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	private Socket socket; // �������

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JPanel contentPane;
	private JTextArea inputTextArea;
	private String UserName;
	private JButton sendButton;
	private JTextPane chatTextPane;
	private JPanel inputPanel;
	private JPanel upperPanel;
	private JLabel titleLabel;
	private JLabel roomImageLabel;
	private JPanel infoPanel;
	private JPanel bottomPanel;
	private JScrollPane inputScrollPane;
	private JPanel textPanel;
	private JLabel countLabel;
	private JPanel listPanel;
	private JLabel personIconLabel;
	private JScrollPane chatScrollPane;
	private JCheckBox sleepCheckBox;

	private Frame frame;
	private FileDialog fd;
	private JButton fileBtn;

	private JList<String> clientList;
	private DefaultListModel<String> clients;

	SendTextAction sendTextAction = new SendTextAction();
	SendFileAction sendImageAction = new SendFileAction();

	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	ImageIcon goodIcon = new ImageIcon("src/good.jpg");
	ImageIcon hahaIcon = new ImageIcon("src/haha.jpg");
	ImageIcon personIcon = new ImageIcon("src/person-fill.png");
	BaseMultiResolutionImage personIc = new BaseMultiResolutionImage(
			personIcon.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH), personIcon.getImage());

	private static final String TEXT_SUBMIT = "text-submit";
	private static final String INSERT_BREAK = "insert-break";

	public JavaObjClientView(String username, String ip_addr, String port_no) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(360, 640);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// TODO: Clients
		clients = new DefaultListModel<String>();
		clientList = new JList<String>(clients);

		// Upper panel
		upperPanel = new JPanel();
		upperPanel.setLayout(new BorderLayout(0, 0));
		contentPane.add(upperPanel, BorderLayout.NORTH);

		infoPanel = new JPanel();
		infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));
		upperPanel.add(infoPanel, BorderLayout.WEST);

		roomImageLabel = new JLabel();
		roomImageLabel.setPreferredSize(new Dimension(40, 40));
		roomImageLabel.setBackground(Color.LIGHT_GRAY);
		roomImageLabel.setOpaque(true);
		infoPanel.add(roomImageLabel);

		textPanel = new JPanel();
		infoPanel.add(textPanel);
		textPanel.setLayout(new BorderLayout(0, 0));

		titleLabel = new JLabel("ä�ù�");
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		titleLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 15));
		textPanel.add(titleLabel, BorderLayout.NORTH);

		listPanel = new JPanel();
		listPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		listPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "200", "/list");
				sendObject(msg);
			}
		});
		textPanel.add(listPanel, BorderLayout.WEST);

		personIconLabel = new JLabel(new ImageIcon(personIc));
		personIconLabel.setHorizontalAlignment(SwingConstants.LEFT);
		personIconLabel.setVerticalAlignment(SwingConstants.TOP);
		personIconLabel.setPreferredSize(new Dimension(14, 10));
		personIconLabel.setBorder(null);
		listPanel.add(personIconLabel);

		countLabel = new JLabel("0"); // TODO: ����� �� ī��Ʈ
		countLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		countLabel.setForeground(Color.GRAY);
		listPanel.add(countLabel);

		// Chat panel
		chatScrollPane = new JScrollPane();
		chatScrollPane.setBorder(null);
		contentPane.add(chatScrollPane, BorderLayout.CENTER);

		chatTextPane = new JTextPane();
		chatTextPane.setEditable(false);
		chatTextPane.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 0));
		chatTextPane.setBackground(SystemColor.window);
		chatTextPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
		chatScrollPane.setViewportView(chatTextPane);

		// Input panel
		inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout(0, 0));
		contentPane.add(inputPanel, BorderLayout.SOUTH);

		inputScrollPane = new JScrollPane();
		inputScrollPane.setBorder(null);
		inputScrollPane.setPreferredSize(new Dimension(0, 80));
		inputPanel.add(inputScrollPane, BorderLayout.CENTER);

		inputTextArea = new JTextArea();
		inputTextArea.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 14));
		inputTextArea.setLineWrap(true);
		inputTextArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		InputMap input = inputTextArea.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		input.put(shiftEnter, INSERT_BREAK);
		input.put(enter, TEXT_SUBMIT);
		inputTextArea.getActionMap().put(TEXT_SUBMIT, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = inputTextArea.getText();
				sendText(msg);
				inputTextArea.setText("");
				inputTextArea.requestFocus();
			}
		});
		inputScrollPane.setViewportView(inputTextArea);

		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		bottomPanel.setLayout(new BorderLayout(0, 0));
		inputPanel.add(bottomPanel, BorderLayout.SOUTH);

		sendButton = new JButton("Send");
		sendButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		sendButton.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		sendButton.setBackground(Color.BLACK);
		sendButton.setForeground(Color.WHITE);
		sendButton.setOpaque(true);
		bottomPanel.add(sendButton, BorderLayout.EAST);

		fileBtn = new JButton("+");
		fileBtn.setFont(new Font("����", Font.PLAIN, 16));
		bottomPanel.add(fileBtn, BorderLayout.WEST);

		sleepCheckBox = new JCheckBox("���� ����");
		sleepCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ChatMsg msg = new ChatMsg(UserName, "200", "/sleep");
					sendObject(msg);
				} else {
					ChatMsg msg = new ChatMsg(UserName, "200", "/wakeup");
					sendObject(msg);
				}
			}
		});
		bottomPanel.add(sleepCheckBox);

		// Initialize
		setVisible(true);
//		appendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;

		fileBtn.addActionListener(sendImageAction);
		sendButton.addActionListener(sendTextAction);
		inputTextArea.requestFocus();

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			sendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			appendText("connect error");
		}
	}

	MainFrame mainFrame = new MainFrame();

	// Server Message�� �����ؼ� ȭ�鿡 ǥ��
	class ListenNetwork extends Thread {
		public void run() {
			StyledDocument doc = chatTextPane.getStyledDocument();
			SimpleAttributeSet left = new SimpleAttributeSet();
			StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			SimpleAttributeSet right = new SimpleAttributeSet();
			StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);

			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("%s", cm.getData());
					} else
						continue;

					ChatPanel chatPanel;
					switch (cm.getCode()) {
					case "100":
						try {
							clients.addElement(cm.getId());
							mainFrame.addFriend(cm.getId());
						} catch (Exception ex) {
						}
						break;
					case "101":
						try {
							clients.removeElement(cm.getId());
							mainFrame.removeFriend(cm.getId());
						} catch (Exception ex) {
						}
						break;
					case "200": // chat message
						if (cm.getId().equals(UserName)) {
							doc.setParagraphAttributes(doc.getLength(), 1, right, false);
							chatPanel = new MyChatPanel();
						} else if (cm.getId().equals("SERVER")) {
							doc.setParagraphAttributes(doc.getLength(), 1, center, false);
							chatPanel = new ServerChatPanel();
						} else {
							doc.setParagraphAttributes(doc.getLength(), 1, left, false);
							chatPanel = new FriendChatPanel(cm.getId());
						}

						if (EMOJI_MAP.containsKey(cm.getData())) {
							appendText("[" + cm.getId() + "]");
							appendImage(EMOJI_MAP.get(cm.getData()));
						} else {
							appendText(msg, chatPanel);
						}
						break;
					case "300": // File ÷��
						if (cm.getId().equals(UserName))
							doc.setParagraphAttributes(doc.getLength(), 1, right, false);
						else
							doc.setParagraphAttributes(doc.getLength(), 1, left, false);
						appendText("[" + cm.getId() + "]");
						appendFile(cm);
						break;
					}
				} catch (IOException e) {
					appendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						socket.close();
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����

			}
		}
	}

	// keyboard enter key ġ�� ������ ����
	class SendTextAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == sendButton || e.getSource() == inputTextArea) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = inputTextArea.getText();
				sendText(msg);
				inputTextArea.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				inputTextArea.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
			}
		}
	}

	class SendFileAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == fileBtn) {
				try {
					frame = new Frame("Attechments");
					fd = new FileDialog(frame, "Select a file", FileDialog.LOAD);
					fd.setVisible(true);
					ChatMsg obcm = new ChatMsg(UserName, "300", "FILE");
					if (fd.getFile() != null && fd.getDirectory() != null) {
						String str = fd.getDirectory() + fd.getFile();
						System.out.println("FileSend: " + str);
						BufferedInputStream bis = null;
						try {
							File sourceFile = new File(str);
							bis = new BufferedInputStream(new FileInputStream(sourceFile));

							byte[] fileBytes = new byte[(int) sourceFile.length()];
							bis.read(fileBytes, 0, fileBytes.length);
							obcm.setFilename(sourceFile.getName());
							obcm.setDataBytes(fileBytes);

							sendObject(obcm);
						} catch (IOException ex) {
							ex.printStackTrace();
						} finally {
							try {
								if (bis != null) {
									bis.close();
								}
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					} else {
						System.out.println("NULL");
					}
				} catch (Exception ex) {
					System.out.println("Exeption!!!");
					ex.printStackTrace();
				}
			}
		}
	}

	private static final Map<String, ImageIcon> EMOJI_MAP;
	static {
		Map<String, ImageIcon> aMap = new HashMap<String, ImageIcon>();
		aMap.put("(haha)", new ImageIcon("src/haha.jpg"));
		aMap.put("(good)", new ImageIcon("src/good.jpg"));
		aMap.put("(cheering)", new ImageIcon("src/icon1.jpg"));
		aMap.put("(����)", new ImageIcon("src/haha.jpg"));
		aMap.put("(��)", new ImageIcon("src/good.jpg"));
		EMOJI_MAP = Collections.unmodifiableMap(aMap);
	}

	// Append icon in textArea
	public void appendIcon(ImageIcon icon) {
		int len = chatTextPane.getDocument().getLength();
		chatTextPane.setCaretPosition(len);
		chatTextPane.insertIcon(icon);
	}

	// Legacy: Append text in textArea
	public void appendText(String msg) {
		int len = chatTextPane.getDocument().getLength();

		msg = msg.trim();
		chatTextPane.setCaretPosition(len);
		chatTextPane.replaceSelection(msg + "\n");
	}

	// Append text in textArea
	public void appendText(String msg, ChatPanel chatPanel) {
		msg = msg.trim();
		chatPanel.setText(msg);

		int len = chatTextPane.getDocument().getLength();
		chatTextPane.setCaretPosition(len);
		chatTextPane.setEditable(true);
		chatTextPane.replaceSelection("\n");
		chatTextPane.insertComponent(chatPanel);

		len = chatTextPane.getDocument().getLength();
		chatTextPane.setCaretPosition(len);
		chatTextPane.replaceSelection("\n");

		len = chatTextPane.getDocument().getLength();
		chatTextPane.setCaretPosition(len);
		chatTextPane.setEditable(false);
	}

	// Append image in textArea
	public void appendImage(ImageIcon ori_icon) {
		int len = chatTextPane.getDocument().getLength();
		chatTextPane.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image�� �ʹ� ũ�� �ִ� ���� �Ǵ� ���� 200 �������� ��ҽ�Ų��.
		if (width > 200 || height > 200) {
			if (width > height) { // ���� ����
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // ���� ����
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon new_icon = new ImageIcon(new_img);
			chatTextPane.insertIcon(new_icon);
		} else
			chatTextPane.insertIcon(ori_icon);
		len = chatTextPane.getDocument().getLength();
		chatTextPane.setCaretPosition(len);
		chatTextPane.replaceSelection("\n");
		// ImageViewAction viewaction = new ImageViewAction();
		// new_icon.addActionListener(viewaction); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
	}

	// Add message to GUI
	public void appendFile(ChatMsg cm) {
		System.out.println("File info: " + cm.getFilename() + ", " + cm.getDataBytes().length);
		File file = createFile(cm.getFilename(), cm.getDataBytes());
		if (file != null) {
			try {
				String mimetype = Files.probeContentType(file.toPath());
				if (mimetype != null && mimetype.contains("image")) {
					System.out.println("It's an image");
					appendImage(new ImageIcon(file.getAbsolutePath()));
				} else {
					System.out.println("It's NOT an image");
					appendText("Sent a File: " + cm.getFilename());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Append file: NULL");
		}
	}

	private SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy");

	private File createFile(String filename, byte[] bytes) {
		BufferedOutputStream bos = null;
		File fileReceive = null;
		try {
			if (filename != null) {
				fileReceive = new File(UserName + "-" + formatter.format(new Date()) + "-" + filename);
				System.out.println(fileReceive.getAbsolutePath());
				bos = new BufferedOutputStream(new FileOutputStream(fileReceive));
				bos.write(bytes);
				bos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return fileReceive;
	}

	// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
	public byte[] makePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Send text to server
	public void sendText(String msg) {
		ChatMsg obcm = new ChatMsg(UserName, "200", msg);
		sendObject(obcm);
	}

	// Send object to server
	public void sendObject(Object ob) {
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			appendText("SendObject Error");
		}
	}
}
