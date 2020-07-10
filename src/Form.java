import org.ini4j.Wini;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalExclusionType;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;


public class Form extends JFrame {

    private JTextField textField;
    private JTextArea textArea_1;
    public JButton serverButton;
    public static int port;
    public static InputStream in;
    public static OutputStream out;
    public static Socket socket = null;
    public static ServerSocket serverSocket = null;
    public static LocalDateTime objLastReceivedMsgDT;
    public static JLabel lblStatus;
    public static Wini objReplyIni = null;
    private final String dir;
    private int iMessageID = 1;
    public static Thread msgReceivedth;
    public static boolean isStart;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Form frame = new Form();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Form() {
        initialize();
        dir = System.getProperty("user.dir");
        GlobalMembers.bAddMsgLen = false;
        GlobalMembers.usMsgLen = 2;
        GlobalMembers.eMsgLenType = GlobalMembers.MsgLenType.ASCII;

        GlobalMembers.bAddISOHeader = false;
        GlobalMembers.usISOHeaderLength = 0;
        GlobalMembers.strDefISOHeader = "";

        GlobalMembers.usMTILength = 4;
        GlobalMembers.strDefMTI = "1200";

        GlobalMembers.bIsHexBitmap = false;
        GlobalMembers.bAddTrailingByte = false;
        GlobalMembers.byTrailingData = 0;
        Log.Write("*********** Switch Application Started***********");
    }

    public void initialize() {
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 663, 514);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBounds(23, 45, 615, 73);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Listening Port No");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(10, 17, 131, 25);
        panel.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(163, 15, 118, 32);
        panel.add(textField);
        textField.setColumns(10);

        serverButton = new JButton("Start Server");
        serverButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        serverButton.setBounds(325, 16, 118, 31);
        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                click_serverButton();
            }
        });
        panel.add(serverButton);

        JLabel lblServerSetting = new JLabel("Server Setting");
        lblServerSetting.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblServerSetting.setHorizontalAlignment(SwingConstants.CENTER);
        lblServerSetting.setBounds(23, 11, 116, 23);
        contentPane.add(lblServerSetting);

        JLabel lblTcpComm = new JLabel("TCP Communication Preview");
        lblTcpComm.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTcpComm.setHorizontalAlignment(SwingConstants.CENTER);
        lblTcpComm.setBounds(23, 129, 218, 31);
        contentPane.add(lblTcpComm);

        lblStatus = new JLabel("Status:-");
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblStatus.setBounds(23, 452, 200, 14);
        contentPane.add(lblStatus);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setBounds(10, 159, 628, 293);
        contentPane.add(panel_1);
        panel_1.setLayout(null);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                textArea_1.setText(null);
            }
        });
        btnClear.setBounds(20, 11, 72, 31);
        panel_1.add(btnClear);
        btnClear.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JButton btnReset = new JButton("Reset Transation");
        btnReset.setBounds(131, 11, 141, 31);
        btnReset.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iMessageID = 1;
                Log.Write("Switch reset");
            }
        });
        panel_1.add(btnReset);

        textArea_1 = new JTextArea();
        textArea_1.setBounds(10, 71, 608, 211);
        panel_1.add(textArea_1);
    }

    public void click_serverButton() {
        try {
            if (serverButton.getText().equals("Start Server")) {
                port = Integer.parseInt(textField.getText());
                if (serverSocket == null) {
                    try {
                        Thread th = new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                System.out.println("in server");
                                try {
                                    serverSocket = new ServerSocket(port);
                                    socket = serverSocket.accept();
                                    if (socket.getRemoteSocketAddress() != null) {
                                        textArea_1.append("\nClient Connected:-" + socket.getRemoteSocketAddress());
                                        Log.Write("Client Connected- " + socket.getRemoteSocketAddress());
                                        in =  new DataInputStream(socket.getInputStream());
                                        out = new DataOutputStream(socket.getOutputStream());
                                        msgReceivedth = new Thread(new Client_MessageReceived());
                                        msgReceivedth.start();
                                        isStart = true;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        th.start();
                        lblStatus.setText("Server started");
                        serverButton.setText("Stop Server");
                        GlobalMembers.ResetISOFields();
                    } catch (Exception e) {
                        Log.Write("Exception In Server Start:-" + e.getMessage());
                    }
                }
                else {
                    lblStatus.setText("Could not create server instance.");
                    Log.Write("Could not create server instance.");
                }
            } else {
                if (serverSocket != null) {
                    if(msgReceivedth != null){
                        //thread stop
                        msgReceivedth.interrupt();
                        isStart =false;
//                        System.out.println("Thread stop");
                    }
                    serverSocket.close(); //Stop the server
                    serverSocket = null;
                }
                lblStatus.setText("Server stopped");
                serverButton.setText("Start Server");
                if (socket != null) {
                    textArea_1.append("\nClient DisConnected:-" + socket.getRemoteSocketAddress());
                    Log.Write("Client DisConnected:-" + socket.getRemoteSocketAddress());
                }
                System.gc();
            }
        } catch (Exception excp) {
            excp.printStackTrace();
            JOptionPane.showMessageDialog(this, excp.getMessage());
        }
    }

     class Client_MessageReceived implements Runnable {
        @Override
        public void run() {
            while (isStart){
                try {
//            System.out.println("isRunning");
                    //reading Msg
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[202];
                        baos.write(buffer, 0, in.read(buffer));
                        objLastReceivedMsgDT = LocalDateTime.now();
                        GlobalMembers.bRequest = Arrays.copyOf(GlobalMembers.bRequest, buffer.length);      //Array Resize
                        System.arraycopy(buffer, 0, GlobalMembers.bRequest, 0, buffer.length);  //Array Copy
                        //convert into byte array
//                          byte[] result = baos.toByteArray();
//                          String res = Arrays.toString(result);
//                          System.out.println("Recieved from client : "+res);
                        Log.Write("Request received (" + socket.getRemoteSocketAddress() + ")- ");
                        Log.Write(GlobalMembers.bRequest);
                        lblStatus.setText("Status - Request received");
                    } catch (IOException e) {
                        // TODO: 10-07-2020  
                        isStart = false;
                        Log.Write("Exception in  reading msg:-" + e.getMessage());
                    }

//                check the ISO message received
                    if (!ISOMsgParser.FilterISO8583Message()) {
                        Log.Write("Request message not proper, waiting for pending data if comes");
                        return;
                    }

                    String strResponseKeyName = "";

                    // if proper then compose the response fields from here
                    if (GlobalMembers.objISO[3].strValue.contains("110000"))          //For BLIR response
                    {
                        strResponseKeyName = "Auth_Req";

                    } else if (GlobalMembers.objISO[3].strValue.contains("920000"))     //For ACK response
                    {
                        strResponseKeyName = "Passbook_Reg";

                    } else  //For FSIR response
                    {
                        strResponseKeyName = "Nack";

                    }

                    //Send Response
                    byte[] fileContent = new byte[0];
                    Log.Write("KeyName for response- " + strResponseKeyName);
                    if (objReplyIni == null)
                        objReplyIni = new Wini(new File(dir + "\\Reply\\Reply.ini"));

                    File file = new File(dir + "\\Reply\\" + strResponseKeyName + ".dat");
                    if (file.exists()) {
                        FileInputStream fin = null;
                        try {
                            // create FileInputStream object
                            fin = new FileInputStream(file);
                            fileContent = new byte[(int) file.length()];
                            fin.read(fileContent);
                            //create string from byte array
//                        String s = new String(fileContent);
//                        System.out.println("File content: " + Arrays.toString(s.getBytes()));
                        } catch (Exception e) {
                            Log.Write("Exception in" + strResponseKeyName + "file Reading:-" + e.getMessage());
                        }
                    } else {
                        Log.Write("File is not exist:-" + strResponseKeyName + ".dat");
                    }
                    if(strResponseKeyName.equals("Auth_Req")){
                        GlobalMembers.bResponse = new byte[(int) file.length() - 2];
                        System.arraycopy(fileContent, 2, GlobalMembers.bResponse, 0, (int) file.length() - 2);
                    }else {
                        GlobalMembers.bResponse = new byte[(int) file.length()];
                        System.arraycopy(fileContent, 0, GlobalMembers.bResponse, 0, (int) file.length());
                    }
                    out.write(GlobalMembers.bResponse);
                    out.flush();
                    lblStatus.setText("Status:- Response sent");
                    Log.Write("Response sent (" + socket.getRemoteSocketAddress() + ")- ");
                    Log.Write(GlobalMembers.bResponse);

                } catch (Exception excp) {
                    Log.Write("Client_MessageReceived()- " + excp.getMessage());
                }
            }
        }
    }
}


//class receivedRequest implements Runnable{
//    @Override
//    public void run() {
//        while (true){
//            try {
//
//            if(!Form.socket.isConnected()){
//                return;
//            }
//                //reading Msg
//                 Form.in = new DataInputStream(Form.socket.getInputStream());
//                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                 byte[] buffer = new byte[202];
//                 baos.write(buffer, 0, Form.in.read(buffer));
////                 objLastReceivedMsgDT = LocalDateTime.now();
//                 GlobalMembers.bRequest = Arrays.copyOf(GlobalMembers.bRequest, buffer.length);      //Array Resize
//                 System.arraycopy(buffer, 0, GlobalMembers.bRequest, 0, buffer.length);  //Array Copy
//
//                 //convert into byte array
////               byte[] result = baos.toByteArray();
////               String res = Arrays.toString(result);
////               System.out.println("Recieved from client : "+res);
//                 Log.Write("Request received (" + Form.socket.getRemoteSocketAddress() + ")- ");
//                 Log.Write(GlobalMembers.bRequest);
//                 Form.lblStatus.setText("Status - Request received");
//            } catch (IOException e) {
//                 Log.Write("Exception in  reading msg:-" + e.getMessage());
//            }
//        }
//    }
//}

