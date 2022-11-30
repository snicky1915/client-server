
// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.io.Serializable;
import java.io.File;
import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:File
	private String data;
	public ImageIcon img;
	public File file;

    private String filename;
    private byte[] dataBytes;

	public ChatMsg(String id, String code, String msg) {
		this.id = id;
		this.code = code;
		this.data = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
    
    public byte[] getDataBytes() {
		return dataBytes;
	}
    
    public String getFilename() {
		return filename;
	}
    
    public void setDataBytes(byte[] dataBytes) {
		this.dataBytes = dataBytes;
	}
    
    public void setFilename(String filename) {
		this.filename = filename;
	}
}