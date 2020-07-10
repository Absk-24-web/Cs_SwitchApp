package Main;

import java.util.Arrays;

public class GlobalMembers {


    static{
        new GlobalMembers();
    }



            //Data members for ISO message composition
            class ISOFields
            {
                //public string strDesc;
                public int iType;
                public int iLength;
                public String strValue;
                //public bool bVisible;
            }
            public static ISOFields[] objISO;

            public enum MsgLenType
            {
                ASCII,
                DECIMAL,
                HEX
            }

            public static byte[] bResponse;
            public static byte[] bRequest;

            public static short usISOHeaderLength;
            public static short usMTILength;
            public static short usMsgLen;

            public static String strDefISOHeader;
            public static String strDefMTI;

            public static boolean bAddMsgLen;
            public static boolean bAddISOHeader;
            public static MsgLenType eMsgLenType;
            public static boolean bIsHexBitmap;
            public static boolean bAddTrailingByte;

            public static byte byTrailingData;

            // Static constructor is called at most one time, before any
            // instance constructor is invoked or member is accessed.
            GlobalMembers()
            {
                objISO = new ISOFields[131];
                for (int i=0; i<=128; i++){
                    objISO[i]=new ISOFields();
                }

                //set the type and length of fields which are coming in request
                //if type is fixed i.e. 0 then length is the fields fixed length
                //if type is variable i.e. 1 then length will be the fields maximum length which that field can receive
                objISO[2].iType = 1; objISO[2].iLength = 17;
                objISO[3].iType = 0; objISO[3].iLength = 6;
                objISO[4].iType = 0; objISO[4].iLength = 12;
                objISO[5].iType = 0; objISO[5].iLength = 12;
                objISO[6].iType = 0; objISO[6].iLength = 12;
                objISO[7].iType = 0; objISO[7].iLength = 10;
                objISO[8].iType = 0; objISO[8].iLength = 8;
                objISO[9].iType = 0; objISO[9].iLength = 8;
                objISO[10].iType = 0; objISO[10].iLength = 8;
                objISO[11].iType = 0; objISO[11].iLength = 6;
                objISO[12].iType = 0; objISO[12].iLength = 12;
                objISO[13].iType = 0; objISO[13].iLength = 4;
                objISO[14].iType = 0; objISO[14].iLength = 4;
                objISO[15].iType = 0; objISO[15].iLength = 6;
                objISO[16].iType = 0; objISO[16].iLength = 4;
                objISO[17].iType = 0; objISO[17].iLength = 4;
                objISO[18].iType = 0; objISO[18].iLength = 4;
                objISO[19].iType = 0; objISO[19].iLength = 3;
                objISO[20].iType = 0; objISO[20].iLength = 3;
                objISO[21].iType = 0; objISO[21].iLength = 3;
                objISO[22].iType = 0; objISO[22].iLength = 12;
                objISO[23].iType = 0; objISO[23].iLength = 3;
                objISO[24].iType = 0; objISO[24].iLength = 3;
                objISO[25].iType = 0; objISO[25].iLength = 4;
                objISO[26].iType = 0; objISO[26].iLength = 4;
                objISO[27].iType = 0; objISO[27].iLength = 1;
                objISO[28].iType = 0; objISO[28].iLength = 6;
                objISO[29].iType = 0; objISO[29].iLength = 3;
                objISO[30].iType = 0; objISO[30].iLength = 24;
                objISO[31].iType = 1; objISO[31].iLength = 99;
                objISO[32].iType = 1; objISO[32].iLength = 11;
                objISO[33].iType = 1; objISO[33].iLength = 11;
                objISO[34].iType = 1; objISO[34].iLength = 28;
                objISO[35].iType = 1; objISO[35].iLength = 37;
                objISO[36].iType = 1; objISO[36].iLength = 104;
                objISO[37].iType = 0; objISO[37].iLength = 16;
                objISO[38].iType = 0; objISO[38].iLength = 6;
                objISO[39].iType = 0; objISO[39].iLength = 3;
                objISO[40].iType = 0; objISO[40].iLength = 3;
                objISO[41].iType = 0; objISO[41].iLength = 8;
                objISO[42].iType = 0; objISO[42].iLength = 15;
                objISO[43].iType = 0; objISO[43].iLength = 40;
                objISO[44].iType = 1; objISO[44].iLength = 99;
                objISO[45].iType = 1; objISO[45].iLength = 76;
                objISO[46].iType = 1; objISO[46].iLength = 204;
                objISO[47].iType = 1; objISO[47].iLength = 999;
                objISO[48].iType = 1; objISO[48].iLength = 999;
                objISO[49].iType = 0; objISO[49].iLength = 3;
                objISO[50].iType = 0; objISO[50].iLength = 3;
                objISO[51].iType = 0; objISO[51].iLength = 3;
                objISO[52].iType = 0; objISO[52].iLength = 16;
                objISO[53].iType = 1; objISO[53].iLength = 48;
                objISO[54].iType = 1; objISO[54].iLength = 120;
                objISO[55].iType = 1; objISO[55].iLength = 255;
                objISO[56].iType = 1; objISO[56].iLength = 35;
                objISO[57].iType = 0; objISO[57].iLength = 3;
                objISO[58].iType = 1; objISO[58].iLength = 11;
                objISO[59].iType = 1; objISO[59].iLength = 999;
                objISO[60].iType = 1; objISO[60].iLength = 999;
                objISO[61].iType = 1; objISO[61].iLength = 999;
                objISO[62].iType = 1; objISO[62].iLength = 999;
                objISO[63].iType = 1; objISO[63].iLength = 999;
                objISO[64].iType = 0; objISO[64].iLength = 16;
                objISO[65].iType = 0; objISO[65].iLength = 16;
                objISO[66].iType = 1; objISO[66].iLength = 204;
                objISO[67].iType = 0; objISO[67].iLength = 2;
                objISO[68].iType = 0; objISO[68].iLength = 3;
                objISO[69].iType = 0; objISO[69].iLength = 3;
                objISO[70].iType = 0; objISO[70].iLength = 3;
                objISO[71].iType = 0; objISO[71].iLength = 8;
                objISO[72].iType = 1; objISO[72].iLength = 999;
                objISO[73].iType = 0; objISO[73].iLength = 6;
                objISO[74].iType = 0; objISO[74].iLength = 10;
                objISO[75].iType = 0; objISO[75].iLength = 10;
                objISO[76].iType = 0; objISO[76].iLength = 10;
                objISO[77].iType = 0; objISO[77].iLength = 10;
                objISO[78].iType = 0; objISO[78].iLength = 10;
                objISO[79].iType = 0; objISO[79].iLength = 10;
                objISO[80].iType = 0; objISO[80].iLength = 10;
                objISO[81].iType = 0; objISO[81].iLength = 10;
                objISO[82].iType = 0; objISO[82].iLength = 10;
                objISO[83].iType = 0; objISO[83].iLength = 10;
                objISO[84].iType = 0; objISO[84].iLength = 10;
                objISO[85].iType = 0; objISO[85].iLength = 10;
                objISO[86].iType = 0; objISO[86].iLength = 16;
                objISO[87].iType = 0; objISO[87].iLength = 16;
                objISO[88].iType = 0; objISO[88].iLength = 16;
                objISO[89].iType = 0; objISO[89].iLength = 16;
                objISO[90].iType = 0; objISO[90].iLength = 10;
                objISO[91].iType = 0; objISO[91].iLength = 3;
                objISO[92].iType = 0; objISO[92].iLength = 3;
                objISO[93].iType = 1; objISO[93].iLength = 11;
                objISO[94].iType = 1; objISO[94].iLength = 11;
                objISO[95].iType = 1; objISO[95].iLength = 99;
                objISO[96].iType = 1; objISO[96].iLength = 999;
                objISO[97].iType = 0; objISO[97].iLength = 17;
                objISO[98].iType = 0; objISO[98].iLength = 25;
                objISO[99].iType = 1; objISO[99].iLength = 11;
                objISO[100].iType = 1; objISO[100].iLength = 11;
                objISO[101].iType = 1; objISO[101].iLength = 17;
                objISO[102].iType = 1; objISO[102].iLength = 25;
                objISO[103].iType = 1; objISO[103].iLength = 28;
                objISO[104].iType = 1; objISO[104].iLength = 100;
                objISO[105].iType = 0; objISO[105].iLength = 16;
                objISO[106].iType = 0; objISO[106].iLength = 16;
                objISO[107].iType = 0; objISO[107].iLength = 10;
                objISO[108].iType = 0; objISO[108].iLength = 10;
                objISO[109].iType = 1; objISO[109].iLength = 84;
                objISO[110].iType = 1; objISO[110].iLength = 84;
                objISO[111].iType = 1; objISO[111].iLength = 999;
                objISO[112].iType = 1; objISO[112].iLength = 999;
                objISO[113].iType = 1; objISO[113].iLength = 999;
                objISO[114].iType = 1; objISO[114].iLength = 999;
                objISO[115].iType = 1; objISO[115].iLength = 999;
                objISO[116].iType = 1; objISO[116].iLength = 999;
                objISO[117].iType = 1; objISO[117].iLength = 999;
                objISO[118].iType = 1; objISO[118].iLength = 999;
                objISO[119].iType = 1; objISO[119].iLength = 999;
                objISO[120].iType = 1; objISO[120].iLength = 999;
                objISO[121].iType = 1; objISO[121].iLength = 999;
                objISO[122].iType = 1; objISO[122].iLength = 999;
                objISO[123].iType = 1; objISO[123].iLength = 999;
                objISO[124].iType = 1; objISO[124].iLength = 999;
                objISO[125].iType = 1; objISO[125].iLength = 999;
                objISO[126].iType = 1; objISO[126].iLength = 999;
                objISO[127].iType = 1; objISO[127].iLength = 999;
                objISO[128].iType = 0; objISO[128].iLength = 16;

            }

            public static void ResetISOFields()
            {
                bRequest = new  byte[0];
                bRequest = Arrays.copyOf(bRequest,0);

                bResponse = new  byte[0];
                bResponse = Arrays.copyOf(bResponse,0);

                strDefMTI = "";

//                for (int i = 0; i < objISO.length; i++)
//                {
//                    objISO[i].strValue = "";
//                }
            }
        }


