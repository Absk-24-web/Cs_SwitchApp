import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Log
{


   public static void Write(String LogData)
   {
       try
       {
//			System.out.println(LogData);

           DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
           Date dateparse = new Date();

           String Path = "C:\\Kiosk_test\\Switch_App";
           File dir = new File(Path);

           if (!dir.exists())
           {
               if (dir.mkdirs())
               {
                   Log.Write("ClientLogs Log Directory Is Created Successfully!");
               }
               else
               {
                   Log.Write("ClientLogs Failed to create multiple directories!");
               }
           }

           File myFile = new File("C:\\Kiosk_test\\Switch_App\\Switch_App_" + date.format(dateparse) + ".txt");

           if (myFile.createNewFile())
           {
//				 System.out.println("File is created!");
           }
           else
           {
//				 System.out.println("File already exists.");
           }
           DateFormat timeparse = new SimpleDateFormat("HH:mm:ss");
           Date time = new Date();

           LogData = timeparse.format(time) + ":- " + LogData + "\n";

           // File file = new File(Path);
           FileWriter fr = null;
           fr = new FileWriter(myFile, true);
           fr.write(LogData);
           fr.close();
       }
       catch (IOException e)
       {
           //e.printStackTrace();
       }
   }


   public static void Write(byte[] byInText)
   {
       try
       {
//			System.out.println(LogData);

           DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
           Date dateparse = new Date();

           String Path = "C:\\Kiosk_test\\Switch_App";
           File dir = new File(Path);

           if (!dir.exists())
           {
               if (dir.mkdirs())
               {
                    Log.Write("ClientLogs Log Directory Is Created Successfully!");
               }
               else
               {
                   Log.Write("ClientLogs Failed to create multiple directories!");
               }
           }

           File myFile = new File("C:\\Kiosk_test\\Switch_App\\Switch_App_" + date.format(dateparse) + ".txt");

           if (myFile.createNewFile())
           {
//				 System.out.println("File is created!");
           }
           else
           {
//				 System.out.println("File already exists.");
           }
           DateFormat timeparse = new SimpleDateFormat("HH:mm:ss");
           Date time = new Date();
           String data = "";

           data = timeparse.format(time) + ":-\n";

           // File file = new File(Path);
           FileOutputStream fr = null;
           fr = new FileOutputStream(myFile, true);
           byte[] bytes = data.getBytes();
           fr.write(bytes);
           fr.write(byInText);
           bytes = "\n".getBytes();
           fr.write(bytes);
           fr.close();
       }
       catch (IOException e)
       {
           //e.printStackTrace();
       }
   }

   public static void Write(String LogData, String msgComm) {
       try
       {
//			System.out.println(LogData);

           DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
           Date dateparse = new Date();

           String Path = "C:\\Kiosk_test\\"+msgComm;
           File dir = new File(Path);

           if (!dir.exists())
           {
               if (dir.mkdirs())
               {
                   Log.Write("ClientLogs Log Directory Is Created Successfully!");
               }
               else
               {
                   Log.Write("ClientLogs Failed to create multiple directories!");
               }
           }

           File myFile = new File("C:\\Kiosk_test\\"+msgComm+"\\MSGCOMM_" + date.format(dateparse) + ".txt");

           if (myFile.createNewFile())
           {
//				 System.out.println("File is created!");
           }
           else
           {
//				 System.out.println("File already exists.");
           }
           DateFormat timeparse = new SimpleDateFormat("HH:mm:ss");
           Date time = new Date();

           LogData = timeparse.format(time) + ":- " + LogData + "\n";

           // File file = new File(Path);
           FileWriter fr = null;
           fr = new FileWriter(myFile, true);
           fr.write(LogData);
           fr.close();
       }
       catch (IOException e)
       {
           //e.printStackTrace();
       }

   }
//    public static void Write(String LogData, String fields) {
//        try
//        {
////			System.out.println(LogData);
//
//            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
//            Date dateparse = new Date();
//
//            String Path = "C:\\Kiosk_test\\"+fields;
//            File dir = new File(Path);
//
//            if (!dir.exists())
//            {
//                if (dir.mkdirs())
//                {
//                    Log.Write("ClientLogs Log Directory Is Created Successfully!");
//                }
//                else
//                {
//                    Log.Write("ClientLogs Failed to create multiple directories!");
//                }
//            }
//
//            File myFile = new File("C:\\Kiosk_test\\"+fields+"\\MSGCOMM_" + date.format(dateparse) + ".txt");
//
//            if (myFile.createNewFile())
//            {
////				 System.out.println("File is created!");
//            }
//            else
//            {
////				 System.out.println("File already exists.");
//            }
//            DateFormat timeparse = new SimpleDateFormat("HH:mm:ss");
//            Date time = new Date();
//
//            LogData = timeparse.format(time) + ":- " + LogData + "\n";
//
//            // File file = new File(Path);
//            FileWriter fr = null;
//            fr = new FileWriter(myFile, true);
//            fr.write(LogData);
//            fr.close();
//        }
//        catch (IOException e)
//        {
//            //e.printStackTrace();
//        }
//
//    }


}