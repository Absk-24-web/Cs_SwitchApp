import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ISOMsgParser {
      public static int  iLen = -1;

    /// <summary>
    /// It converts Hex value to its corresponding Binary format
    /// </summary>
    /// <param name="strHex">Hex format value</param>
    /// <returns>Binary format value</returns>
    public static String ConvertHexToBinary(String strHex)
    {
        String output = "";
        if (strHex.length() % 4 != 0) return "";
        strHex = strHex.replace(" ","");
        output =  new BigInteger(strHex, 16).toString(2);
        return output;
    }

    /// <summary>
    /// It converts Hex String in its ASCII byte array
    /// </summary>
    /// <param name="strHex">String in Hex format</param>
    /// <returns>ASCII byte array</returns>
//    public static byte[] ConvertHexToAscii(String strHex)
//    {
//        byte[] Asc = new byte[strHex.Length / 2];
//        for (int i = 0; i < Asc.Length; i++)
//            Asc[i] = Convert.ToByte(strHex.substring(i * 2, 2), 16);
//        return Asc;
//    }


    /// <summary>
    /// Convert bytes in ASCII to its Hex Format
    /// </summary>
    /// <param name="byteAsc">ASCII byte array</param>
    /// <returns>String in Hex format</returns>
    public static String ConvertAsciiToHex(byte[] byteAsc)
    {
        StringBuilder retHexstr = new StringBuilder();
        for (byte c : byteAsc)
        {
//            retHexstr.append(Integer.parseInt(String.valueOf(c))toString("X").PadLeft(2, '0'));
            retHexstr.append(String.format("%02X ", c));
        }
        return retHexstr.toString();
    }

   public static String ConvertBinaryToHex(String strBinary)
    {
        StringBuilder strAscii = new StringBuilder();
        for (int i = 0; i < strBinary.length(); i+=4)
        {
            double sum = 0;
            String strHex = strBinary.substring(i, i+4);

            for (int j = 0; j<strHex.length(); j++)
            {
                String s = String.valueOf(strHex.charAt(j));
                if (s.equals("1"))
                    sum += Math.pow(2, 3 - j);
            }
            strAscii.append(Integer.toHexString((int) sum).toUpperCase());
        }
        return strAscii.toString();
    }


   public static boolean FilterISO8583Message(  )
    {
        try
        {
            byte[] bBitmap = new byte[0];
            String strBinary = "";
            int iMTIPos = 0;
            int iBitmapPos = 0;
            int iFieldsPos = 0;

            //If msg len is coming in request
            if (GlobalMembers.bAddMsgLen)
            {
                iMTIPos = GlobalMembers.usMsgLen;
                iBitmapPos = GlobalMembers.usMsgLen;
                iFieldsPos = GlobalMembers.usMsgLen;
            }

            //if ISO header exist
            if (GlobalMembers.bAddISOHeader)
            {
                iMTIPos += GlobalMembers.usISOHeaderLength;
                iBitmapPos += GlobalMembers.usISOHeaderLength;
                iFieldsPos += GlobalMembers.usISOHeaderLength;
            }

            //take out MTI
            byte[] byMTI = new byte[GlobalMembers.usMTILength];
            System.arraycopy(GlobalMembers.bRequest,iMTIPos,byMTI,0,GlobalMembers.usMTILength); //Array Copy
            GlobalMembers.strDefMTI = new String(byMTI, StandardCharsets.UTF_8);

            //increase position by MTI length
            iBitmapPos += GlobalMembers.usMTILength;
            iFieldsPos += GlobalMembers.usMTILength;

            //if bitmap is in HEX
            if (GlobalMembers.bIsHexBitmap)
            {
                bBitmap = Arrays.copyOf(bBitmap,bBitmap.length+32); //Array Resize
                System.arraycopy(GlobalMembers.bRequest,iBitmapPos,bBitmap,0,32); //Array copy
                iFieldsPos += 32;
                strBinary = ISOMsgParser.ConvertHexToBinary(Arrays.toString(bBitmap));
            }
            else    //if bitmap is in ASCII
            {
                bBitmap = Arrays.copyOf(bBitmap,bBitmap.length+16); //Array Resize
                System.arraycopy(GlobalMembers.bRequest,iBitmapPos,bBitmap,0,16); //Array Copy
                iFieldsPos += 16;
                strBinary = ISOMsgParser.ConvertHexToBinary(ISOMsgParser.ConvertAsciiToHex(bBitmap));
            }

//            String strMsgFields = Encoding.ASCII.GetString(GlobalMembers.bRequest, iFieldsPos, GlobalMembers.bRequest.length - iFieldsPos);
            int arrayLen = GlobalMembers.bRequest.length - iFieldsPos;
            byte [] toEncode =new byte[arrayLen];
            System.arraycopy(GlobalMembers.bRequest,iFieldsPos,toEncode,0,arrayLen);
            String strMsgFields = new String(toEncode, StandardCharsets.UTF_8);


            for (int iField = 1; iField < strBinary.length(); iField++)
            {
                //if its bitmap is 1
                String s = String.valueOf(strBinary.charAt(iField));
                if (s.equals("1"))
                {
                    //if it is fixed type field
                    if (GlobalMembers.objISO[iField + 1].iType == 0)
                    {
                        //if its length is equal to remaining msg
                        if (GlobalMembers.objISO[iField + 1].iLength == strMsgFields.length())
                            GlobalMembers.objISO[iField + 1].strValue = strMsgFields;
                        else if (GlobalMembers.objISO[iField + 1].iLength < strMsgFields.length())     //if its length is smaller than remaining msg
                        {
                            //take out the msg as field value
                            GlobalMembers.objISO[iField + 1].strValue = strMsgFields.substring(0, GlobalMembers.objISO[iField + 1].iLength);

                            //trim field data
                            strMsgFields = strMsgFields.substring(GlobalMembers.objISO[iField + 1].iLength);
                        }
                        else
                        {
                            Log.Write("Field " + (iField + 1) + " (FIXED) data is not proper, its length should be " + GlobalMembers.objISO[iField + 1].iLength + " bytes");
                            return false;
                        }
                    }
                    else    //if it is variable length field
                    {
//                        if (!int.TryParse(strMsgFields.Substring(0, GlobalMembers.objISO[iField + 1].iLength.ToString().Length), out iLen))
                        int a = Integer.toString(GlobalMembers.objISO[iField + 1].iLength).length();
                        if (!isNumber(strMsgFields.substring(0,a)))
                        {
                            Log.Write("Field " + (iField + 1) + " (VARIABLE) length data is not proper, its length data should be " + GlobalMembers.objISO[iField + 1].iLength + " bytes");
                            return false;
                        }

                        //trim length before field data
                        strMsgFields = strMsgFields.substring(Integer.toString(GlobalMembers.objISO[iField + 1].iLength).length());

                        //if length is equal to remaining msg length
                        if (iLen == strMsgFields.length())
                            GlobalMembers.objISO[iField + 1].strValue = strMsgFields;
                        else if (iLen < strMsgFields.length())        //if its length is smaller than remaining msg
                        {
                            //take out the msg as field value
                            GlobalMembers.objISO[iField + 1].strValue = strMsgFields.substring(0, iLen);
                            //trim field data
                            strMsgFields = strMsgFields.substring(iLen);
                        }
                        else
                        {
                            Log.Write("Field"+(iField)+"(VARIABLE) data is not proper, its length should be"+iLen+"bytes");
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        catch (Exception excp)
        {
            Log.Write("Exception in filter:-" +excp.getMessage());
            return false;
        }
        finally
        { System.gc(); }
    }


    private static boolean isNumber(String str){
        try{
            iLen = Integer.parseInt(str);
//            System.out.println(str+" "+a);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public static boolean ComposeISO8583Message( byte[] byResponse,  String strErrorMsg)
    {
        try
        {
            String strReplyMsg = "";
            StringBuilder strReplyBitmap = new StringBuilder("1");
            strErrorMsg = "";

            for (int iField = 1; iField < 128; iField++)
            {
                if (GlobalMembers.objISO[iField + 1].strValue != null && GlobalMembers.objISO[iField + 1].strValue != "")
                {
                    strReplyBitmap.append("1");
                    if (GlobalMembers.objISO[iField + 1].iType == 0)
                    {
                        if (GlobalMembers.objISO[iField + 1].iLength == GlobalMembers.objISO[iField + 1].strValue.length())
                            strReplyMsg += GlobalMembers.objISO[iField + 1].strValue;
                        else
                        {
                            strErrorMsg = "ISO Field " + (iField + 1) + " data is not proper, its length should be " + GlobalMembers.objISO[iField + 1].iLength + " bytes";
                            return false;
                        }
                    }
                    else
                    {
                        int iRes = -1;
                        int a = (GlobalMembers.objISO[iField + 1].iLength);
                        if (Integer.parseInt(String.valueOf(a)) != 0)
                        {
                            if (GlobalMembers.objISO[iField + 1].strValue.length() <= GlobalMembers.objISO[iField + 1].iLength)
                            {
                                strReplyMsg += GlobalMembers.objISO[iField + 1].strValue.length() ;
                                strReplyMsg += GlobalMembers.objISO[iField + 1].strValue;
                            }
                            else
                            {
                                strErrorMsg = "ISO Field " + (iField + 1) + " data is not proper, its maximum length could be " + GlobalMembers.objISO[iField + 1].iLength + " bytes";
                                return false;
                            }
                        }
                    }
                }
                else
                {
                    strReplyBitmap.append("0");
                }
            }

            //if msg length is to be added
            if (GlobalMembers.bAddMsgLen)
            {

                int iMsgLen = ((GlobalMembers.bAddMsgLen) ? GlobalMembers.usMsgLen : 0) + ((GlobalMembers.bIsHexBitmap) ? 32 : 16) + strReplyMsg.length() + ((GlobalMembers.bAddTrailingByte) ? 1 : 0);

                byte[] bMsgLen = new byte[1];

                //if length is to be added in HEX
                if (GlobalMembers.eMsgLenType == GlobalMembers.MsgLenType.HEX)  //For V1.0.0.2
                {
                    String strHexLen = iMsgLen + ("X" + GlobalMembers.usMsgLen);

                    bMsgLen = Arrays.copyOf(bMsgLen , GlobalMembers.usMsgLen + 1);
                    System.arraycopy(strHexLen.getBytes(StandardCharsets.US_ASCII), 0, bMsgLen, 0,  GlobalMembers.usMsgLen);
                }
                else if (GlobalMembers.eMsgLenType == GlobalMembers.MsgLenType.ASCII)   //if to be added in ASCII
                {
                    //convert to HEX first
                    String strHexLen = iMsgLen+("X" + GlobalMembers.usMsgLen * 2);

                    //convert to its ASCII
                    for (int i = 0; i < strHexLen.length(); i += 2)
                    {
                        bMsgLen = Arrays.copyOf(bMsgLen , bMsgLen.length + 1);
                        bMsgLen[bMsgLen.length - 2] = Byte.parseByte(String.valueOf(Integer.parseInt(strHexLen.substring(i, 2), 16)));
                    }
                }
                else
                {
                    //convert to DECIMAL length in String first
                    String strHexLen = iMsgLen+("D" + GlobalMembers.usMsgLen);

                    bMsgLen = Arrays.copyOf(bMsgLen,GlobalMembers.usMsgLen + 1);
                    System.arraycopy(strHexLen.getBytes(StandardCharsets.US_ASCII), 0, bMsgLen, 0,  GlobalMembers.usMsgLen);
                }

                //add msg len in final reply
                byResponse = Arrays.copyOf(byResponse, GlobalMembers.usMsgLen);
                System.arraycopy(bMsgLen, 0, byResponse, 0,  GlobalMembers.usMsgLen);
            }

            //if ISO header is to be added
            if (GlobalMembers.bAddISOHeader)
            {
                if (GlobalMembers.usISOHeaderLength != GlobalMembers.strDefISOHeader.length())
                {
                    strErrorMsg = "ISO Header length is not proper";
                    return false;
                }

                //add ISO header in final reply
                byResponse = Arrays.copyOf( byResponse, byResponse.length + GlobalMembers.usISOHeaderLength);
                System.arraycopy(GlobalMembers.strDefISOHeader.getBytes(StandardCharsets.US_ASCII), 0, byResponse, byResponse.length - GlobalMembers.usISOHeaderLength, GlobalMembers.usISOHeaderLength);
            }

            if (GlobalMembers.strDefMTI.length() != GlobalMembers.usMTILength)
            {
                strErrorMsg = "MTI length is not proper";
                return false;
            }

            //add MTI in final reply
            byResponse = Arrays.copyOf(byResponse, byResponse.length + GlobalMembers.usMTILength);
            System.arraycopy(GlobalMembers.strDefMTI.getBytes(StandardCharsets.US_ASCII), 0, byResponse, byResponse.length - GlobalMembers.usMTILength, GlobalMembers.usMTILength);

            //if bitmap required in HEX
            if (GlobalMembers.bIsHexBitmap)
            {
                //add HEX bitmap in final reply
                byResponse = Arrays.copyOf(byResponse,byResponse.length+32);
                System.arraycopy(ISOMsgParser.ConvertBinaryToHex(strReplyBitmap.toString()).getBytes(StandardCharsets.US_ASCII), 0, byResponse, byResponse.length - 32, 32);
            }
            else
            {
                //add ASCII bitmap in final reply
                byResponse = Arrays.copyOf(byResponse,byResponse.length+16);
//                System.arraycopy(ISOMsgParser.ConvertHexToAscii(ISOMsgParser.ConvertBinaryToHex(strReplyBitmap.toString())), 0, byResponse, byResponse.length - 16, 16);
            }

            //add fields in final reply
            byResponse = Arrays.copyOf(byResponse,byResponse.length+strReplyMsg.length()); //array resize
            System.arraycopy(strReplyMsg.getBytes(StandardCharsets.US_ASCII),0,byResponse,byResponse.length-strReplyMsg.length(),strReplyMsg.length()); //array copy

            //if trailing byte to be added
            if (GlobalMembers.bAddTrailingByte)
            {
                //add trailing byte in final reply
                byResponse = Arrays.copyOf(byResponse,byResponse.length+1);  //Array Resize
                byResponse[byResponse.length - 1] = GlobalMembers.byTrailingData;
            }

            return true;
        }
        catch (Exception excp)
        {
            strErrorMsg = excp.getMessage();
            return false;
        }
        finally
        {
            System.gc();
        }
    }
}
