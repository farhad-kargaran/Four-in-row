package ir11.co.tsco.four_in_row;

/**
 * Created by Farhad on 4/11/2016.
 */
public class Helper {

    private static final int EASY = 2;
    private static final int MEDIUM = 3;
    private static final int HARD = 4;
    private static int difficulty = 4;

    public static int [][] findAllLegalMoves(byte[][] board) {
        int [][] legalMove = {
                {-1,-1},
                {-1,-1},
                {-1,-1},
                {-1,-1},
                {-1,-1},
                {-1,-1},
                {-1,-1}
        };

        for(int c = 0;c < 7;c++) {
            for(int r = 5;r >= 0;r--) {
                if(board[r][c] == 0) {
                    legalMove[c][0] = r;
                    legalMove[c][1] = c;
                    break;
                }
            }
        }
        return legalMove;
    }

    public static boolean isDraw(byte[][]board) {
        for(int row = 0;row < 5;row++) {
            for(int col = 0;col < 7;col++) {
                if(board[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }


    public static int analysisIt(byte[][] board) {
        int res = 0;




        return res;
    }


    public static byte isBuggy(byte[][] board, byte color)
    {
        byte otherColor = 1;
        if(color == 1)otherColor = 2;

        for(byte i=0;i<6;i++)
        {
            byte min = (byte) ((i * 7) + 1);
            for(int j = 0;j<=2;j++) {
                if (board[(min+j)/7][(min+j)%7] == otherColor && board[(min+j+1)/7][(min+j+1)%7] == otherColor && board[(min-1+j)/7][(min-1+j)%7] == 0 && board[(min+j+2)/7][(min+j+2)%7] == 0) {
                    if (i == 5) return Helper.Column_Of(min + j - 1);
                    else  if(board[(min-1+j+7)/7][(min-1+j+7)%7] != 0 && board[(min+j+2+7)/7][(min+j+2+7)%7] != 0)
                        return Helper.Column_Of(min + j - 1);
                }
            }
        }
        return -1;
    }
    public static byte isBuggable(byte zCol, byte[][] board,byte color)
    {
        byte otherColor = 1;
        if(color == 1)otherColor = 2;
        byte[][] tmp_boardValues = new byte[][]{
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
        };

        for(byte i=0;i<=5;++i)
            for(byte j=0;j<=6;++j)
                tmp_boardValues[i][j] = board[i][j];
        byte index = Helper.Find_Empty_Index_By_Column(zCol,tmp_boardValues);
        tmp_boardValues[index/7][index%7] = color;

        for(byte i=0;i<6;i++)
        {
            byte min = (byte) ((i * 7) + 1);
            for(int j = 0;j<=2;j++) {
                if (tmp_boardValues[(min+j)/7][(min+j)%7] == otherColor && tmp_boardValues[(min+j+1)/7][(min+j+1)%7] == otherColor && tmp_boardValues[(min-1+j)/7][(min-1+j)%7] == 0 && tmp_boardValues[(min+j+2)/7][(min+j+2)%7] == 0) {
                    if (i == 5) return Helper.Column_Of(min + j - 1);
                    else  if(tmp_boardValues[(min-1+j+7)/7][(min-1+j+7)%7] != 0 && tmp_boardValues[(min+j+2+7)/7][(min+j+2+7)%7] != 0)
                        return Helper.Column_Of(min + j - 1);
                }
            }
        }
        return -1;
    }
    public static boolean isBlackList(byte zCol, byte[][] board)
    {
        byte[][] tmp_boardValues = new byte[][]{
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
        };

        for(byte i=0;i<=5;++i)
            for(byte j=0;j<=6;++j)
                tmp_boardValues[i][j] = board[i][j];


        byte index = Helper.Find_Empty_Index_By_Column(zCol,tmp_boardValues);
        tmp_boardValues[index/7][index%7] = 2;
        byte res = Helper.checkIfCanWin((byte) 1,tmp_boardValues);
        if(res == -1) return  false;
        return true;
    }

    public static  byte checkIfCanWin(byte color, byte[][] board)
    {
        byte res=-1;
        byte[][] tmp_boardValues = new byte[][]{
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
        };

        for(byte i=0;i<=5;++i)
            for(byte j=0;j<=6;++j)
                tmp_boardValues[i][j] = board[i][j];

        for(byte i=0;i<7;i++)
        {
            byte z_index = Helper.Find_Empty_Index_By_Column(i,tmp_boardValues);
            if(z_index == -1)continue;
            tmp_boardValues[z_index/7][z_index%7] = color;
            if(Helper.checkIfWin(z_index,color,tmp_boardValues,false))
                return z_index;

            tmp_boardValues[z_index/7][z_index%7] = 0;
        }


        return res;

    }


    public static boolean checkIfWin(byte targetIndex_zeroBased, byte color, byte[][] board, boolean initial_row_col)
    {

        //Log.i("ffff","checkIfWin");
        byte counter = 0;
        byte xx;
        //Chechk row
        byte min = (byte) Helper.MinInRow(targetIndex_zeroBased);
        for(byte i=0;i<7;i++)
        {
            byte a = (byte) ((min + i)/7);
            byte b = (byte) ((min + i)%7);
            xx = board[a][b];
            if (xx == color) {
                counter++;
                if(initial_row_col)
                {
                    if(counter == 1)
                        GameView.t1 = (byte) (min + i);
                    if(counter == 2)
                        GameView.t2 = (byte) (min + i);
                    if(counter == 3)
                        GameView.t3 = (byte) (min + i);
                    if(counter == 4)
                        GameView.t4 = (byte) (min + i);
                }
                if(counter == 4) return true;
            }
            else counter = 0;
        }

        byte tindex = targetIndex_zeroBased;
        counter = 1;
        byte row = (byte) (targetIndex_zeroBased/7);
        byte col = (byte) (targetIndex_zeroBased%7);
        if(initial_row_col)
            GameView.t1 = tindex;
        //Chechk col
        if(targetIndex_zeroBased + 21 <= 41) {
            for (byte i = 1; i <= 3; i++) {

                tindex+=7;
                row++;
                byte a = row;
                byte b = col;
                xx = board[a][b];
                if (xx == color) {
                    counter++;
                    if(initial_row_col)
                    {
                        if(counter == 2)
                            GameView.t2 = tindex;
                        if(counter == 3)
                            GameView.t3 = tindex;
                        if(counter == 4)
                            GameView.t4 = tindex;
                    }
                    if (counter == 4) return true;
                } else break;
            }
        }
       /* counter = 1;
        //Chechk col
        if(targetIndex_zeroBased + 21 <= 41) {
            for (byte i = 1; i <= 3; i++) {

                byte a = (byte) ((targetIndex_zeroBased + (i*7))/7);
                byte b = (byte) ((targetIndex_zeroBased + (i*7))%7);
                xx = board[a][b];
                if (xx == color) {
                    counter++;
                    if (counter == 4) return true;
                } else break;
            }
        }*/

        counter = 0;
        //Check x1 row
        byte minx1 = Helper.MinInX1(targetIndex_zeroBased);
        byte maxx1 = Helper.MaxInX1(targetIndex_zeroBased);
        while (minx1 <= maxx1)
        {
            byte a = (byte) ((minx1)/7);
            byte b = (byte) ((minx1)%7);
            xx = board[a][b];
            if(xx == color)
            {
                counter++;
                if(initial_row_col)
                {
                    if(counter == 1)
                        GameView.t1 = minx1;
                    if(counter == 2)
                        GameView.t2 = minx1;
                    if(counter == 3)
                        GameView.t3 = minx1;
                    if(counter == 4)
                        GameView.t4 = minx1;
                }
                if (counter == 4) return true;
            } else counter = 0;
            minx1 += 8;
        }

        counter = 0;
        //Check x2 row
        byte minx2 = Helper.MinInX2(targetIndex_zeroBased);
        byte maxx2 = Helper.MaxInX2(targetIndex_zeroBased);
        while (minx2 <= maxx2)
        {

            byte a = (byte) ((minx2)/7);
            byte b = (byte) ((minx2)%7);
            xx = board[a][b];
            if(xx == color)
            {
                counter++;
                if(initial_row_col)
                {
                    if(counter == 1)
                        GameView.t1 = minx2;
                    if(counter == 2)
                        GameView.t2 = minx2;
                    if(counter == 3)
                        GameView.t3 = minx2;
                    if(counter == 4)
                        GameView.t4 = minx2;
                }
                if (counter == 4) return true;
            } else counter = 0;
            minx2 += 6;
        }

        return false;
    }

    public static byte Column_Of(int zIndex)
    {
        return (byte) (zIndex % 7);
    }
    public static byte Find_Empty_Index_By_Column(byte zColumn, byte[][] board)
    {
        byte res = -1;
        for(byte i=0;i<6;i++)
        {
            if(board[i][zColumn] == 0)
                res = (byte) (zColumn + (i * 7));
            else break;
        }

        return res;
    }

    public static int MaxInRow(int targetIndex)
    {
        int row = targetIndex / 7;
        row++;
        return (row * 7 - 1);
    }
    public static int MinInRow(int targetIndex)
    {
        int row = targetIndex / 7;
        row++;
        return (row * 7 - 7);
    }
    public static byte MinInX1(byte target)
    {
        byte row = (byte) (target / 7);
        row++;
        while((target-8 >=0) && (row-1) == ((target-8)/7)+1)
        {
            target = (byte) (target-8);
            row = (byte) (target / 7);
            row++;
        }
        return target;
    }

    public static byte MaxInX1(byte target)
    {
        int row = target / 7;
        row++;
        while((target+8 <=41) && (row+1) == ((target+8)/7)+1)
        {
            target = (byte) (target+8);
            row = target / 7;
            row++;
        }
        return target;
    }
    public static byte MinInX2(byte target)
    {
        byte row = (byte) (target / 7);
        row++;
        while((target-6 >=0) && (row-1) == ((target-6)/7)+1)
        {
            target = (byte) (target-6);
            row = (byte) (target / 7);
            row++;
        }
        return target;
    }
    public static byte MaxInX2(byte target)
    {
        byte row = (byte) (target / 7);
        row++;
        while((target+6 <= 41) && (row+1) == ((target+6)/7)+1)
        {
            target = (byte) (target+6);
            row = (byte) (target / 7);
            row++;
        }
        return target;
    }

    public static int analysis(byte[][] board) {
        int whoWon = 0;


        // check 1 valid of 4 and other 0 in row -----------------------------------START--------------------------------------------------

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 1 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
                    whoWon = whoWon - 125;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
                    whoWon = whoWon - 125;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 1 && board[row][col + 3] == 0) {
                    whoWon = whoWon - 125;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 1) {
                    whoWon = whoWon - 125;
                }
            }
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 2 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
                    whoWon = whoWon + 125;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 2 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
                    whoWon = whoWon + 125;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 2 && board[row][col + 3] == 0) {
                    whoWon = whoWon + 125;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 2) {
                    whoWon = whoWon + 125;
                }
            }
        }
        // check 1 valid of 4 and other 0 in row -----------------------------------END--------------------------------------------------




        // check 2 valid of 4 and other 0 in row -----------------------------------START--------------------------------------------------
        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 1 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
                    whoWon = whoWon - 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 1 && board[row][col + 2] == 1 && board[row][col + 3] == 0) {
                    whoWon = whoWon - 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 1 && board[row][col + 3] == 1) {
                    whoWon = whoWon - 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 1 && board[row][col + 1] == 0 && board[row][col + 2] == 1 && board[row][col + 3] == 0) {
                    whoWon = whoWon - 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 1) {
                    whoWon = whoWon - 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 1 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 1) {
                    whoWon = whoWon - 250;
                }
            }
        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 2 && board[row][col + 1] == 2 && board[row][col + 2] == 0 && board[row][col + 3] == 0) {
                    whoWon = whoWon + 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 2 && board[row][col + 2] == 2 && board[row][col + 3] == 0) {
                    whoWon = whoWon + 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 0 && board[row][col + 2] == 2 && board[row][col + 3] == 2) {
                    whoWon = whoWon + 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 2 && board[row][col + 1] == 0 && board[row][col + 2] == 2 && board[row][col + 3] == 0) {
                    whoWon = whoWon + 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 0 && board[row][col + 1] == 2 && board[row][col + 2] == 0 && board[row][col + 3] == 2) {
                    whoWon = whoWon + 250;
                }
            }
        }

        for(int col = 0;col <= 3;col++) {
            for(int row = 0;row < 6;row++) {
                if(board[row][col] == 2 && board[row][col + 1] == 0 && board[row][col + 2] == 0 && board[row][col + 3] == 2) {
                    whoWon = whoWon + 250;
                }
            }
        }
        // check 2 valid of 4 and other 0 in row -----------------------------------END--------------------------------------------------





        // check 3 valid of 4 and other 0 in row -----------------------------------START--------------------------------------------------

        for (int col = 0; col <= 3; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] == 1 && board[row][col + 1] == 1 && board[row][col + 2] == 1 && board[row][col + 3] == 0) {
                    whoWon = whoWon - 1000;
                }
            }
        }

        for (int col = 0; col <= 3; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] == 0 && board[row][col + 1] == 1 && board[row][col + 2] == 1 && board[row][col + 3] == 1) {
                    whoWon = whoWon - 1000;
                }
            }
        }

        for (int col = 0; col <= 3; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] == 1 && board[row][col + 1] == 0 && board[row][col + 2] == 1 && board[row][col + 3] == 1) {
                    whoWon = whoWon - 1000;
                }
            }
        }

        for (int col = 0; col <= 3; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] == 1 && board[row][col + 1] == 1 && board[row][col + 2] == 0 && board[row][col + 3] == 1) {
                    whoWon = whoWon - 1000;
                }
            }
        }
        //++++++++++++++++++++++++++++++++++++++++++++


        for (int col = 0; col <= 3; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] == 2 && board[row][col + 1] == 2 && board[row][col + 2] == 2 && board[row][col + 3] == 0) {
                    whoWon = whoWon + 1000;
                }
            }
        }

        for (int col = 0; col <= 3; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] == 0 && board[row][col + 1] == 2 && board[row][col + 2] == 2 && board[row][col + 3] == 2) {
                    whoWon = whoWon + 1000;
                }
            }
        }

        for (int col = 0; col <= 3; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] == 2 && board[row][col + 1] == 0 && board[row][col + 2] == 2 && board[row][col + 3] == 2) {
                    whoWon = whoWon + 1000;
                }
            }
        }

        for (int col = 0; col <= 3; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] == 2 && board[row][col + 1] == 2 && board[row][col + 2] == 0 && board[row][col + 3] == 2) {
                    whoWon = whoWon + 1000;
                }
            }
        }

        // check 3 valid of 4 and other 0 in row -----------------------------------END--------------------------------------------------




        //if(GameView.redTurn == false) {



           /* if(isBuggy(board, (byte) 1) != -1)
            {
                whoWon = whoWon + 2000 ;
            }

            if(isBuggy(board, (byte) 2) != -1)
            {
                whoWon = whoWon - 2000 ;
            }*/

            int cost = 250;
            // check any valid of 4 and other 0 in x1 & x2 rows -----------------------------------START--------------------------------------------------
            for (byte i = 0; i <= 5; i++) {

                if (i == 4) i = 7;
                if (i == 5) i = 14;
                byte y = 0, r = 0,yy=0,rr=0; // y stand for yellow and r for red
                boolean re = false, ye = false; //re stand for red, ye = yellow
                //Check x1 row
                byte minx1 = Helper.MinInX1(i);
                byte maxx1 = Helper.MaxInX1(i);

                while (minx1 <= maxx1) {
                    byte a = (byte) ((minx1) / 7);
                    byte b = (byte) ((minx1) % 7);
                    byte xx = board[a][b];
                    if (xx == 1)
                        re = true;
                    if (xx == 2)
                        ye = true;
                    if (xx == 1 || xx == 0) {
                        r++;
                        y = 0;
                    }
                    if (xx == 2 || xx == 0) {
                        y++;
                        r = 0;
                    }
                    if (xx == 1) {
                        rr++;
                        yy = 0;
                    }
                    if (xx == 2) {
                        yy++;
                        rr = 0;
                    }
                    if (xx == 0) {
                        yy = 0;
                        rr = 0;
                    }
                    if (rr >= 2) {
                        whoWon = whoWon - (cost * r);
                        rr = 0;
                    }
                    if (yy >= 2) {
                        whoWon = whoWon + (cost * y);
                        yy = 0;
                    }
                    if (r >= 4 && re) {
                        whoWon = whoWon - (cost * r);
                        if( (minx1+8) <= maxx1) {
                            minx1 = (byte) (minx1 - 24);
                            r = 0;
                            re = false;
                        }
                    }
                    if (y >= 4 && ye) {
                        whoWon = whoWon + (cost * y);
                        if( (minx1+8) <= maxx1) {
                            minx1 = (byte) (minx1 - 24);
                            y = 0;
                            ye = false;
                        }
                    }
                    minx1 += 8;
                }
                if (i == 7) i = 4;
                if (i == 14) i = 5;
            }
            //----------------------------X2---------------------------------
            for (byte i = 3; i <= 8; i++) {

                if (i == 7) i = 13;
                if (i == 8) i = 20;
                byte y = 0, r = 0,yy=0,rr=0; // y stand for yellow and r for red
                boolean re = false, ye = false; //re stand for red, ye = yellow
                //Check x1 row
                byte minx2 = Helper.MinInX2(i);
                byte maxx2 = Helper.MaxInX2(i);

                while (minx2 <= maxx2) {
                    byte a = (byte) ((minx2) / 7);
                    byte b = (byte) ((minx2) % 7);
                    byte xx = board[a][b];
                    if (xx == 1)
                        re = true;
                    if (xx == 2)
                        ye = true;
                    if (xx == 1 || xx == 0) {
                        r++;
                        y = 0;
                    }
                    if (xx == 2 || xx == 0) {
                        y++;
                        r = 0;
                    }
                    if (xx == 1) {
                        rr++;
                        yy = 0;
                    }
                    if (xx == 2) {
                        yy++;
                        rr = 0;
                    }
                    if (xx == 0) {
                        yy = 0;
                        rr = 0;
                    }
                    if (rr >= 2) {
                        whoWon = whoWon - (cost * r);
                        rr = 0;
                    }
                    if (yy >= 2) {
                        whoWon = whoWon + (cost * y);
                        yy = 0;
                    }
                    if (r >= 4 && re) {
                        whoWon = whoWon - (cost * r);
                        if( (minx2+6) <= maxx2) {
                            minx2 = (byte) (minx2 - 18);
                            r = 0;
                            re = false;
                        }
                    }
                    if (y >= 4 && ye) {
                        whoWon = whoWon + (cost * y);
                        if( (minx2+6) <= maxx2) {
                            minx2 = (byte) (minx2 - 18);
                            y = 0;
                            ye = false;
                        }
                    }
                    minx2 += 6;
                }
                if (i == 13) i = 7;
                if (i == 20) i = 8;
            }
        //}
        // check any valid of 4 and other 0 in x1 & x2 rows -----------------------------------START--------------------------------------------------
        //if(difficulty == hard)




        return whoWon;
    }
    public static  int testForWinner(byte[][] board) {

        //Check for vertical win
        for(int c = 0;c < 7;c++) {
            for(int r = 5;r >= 3;r--) {
                if(board[r][c] == 1 && board[r - 1][c] == 1 && board[r - 2][c] == 1 && board[r - 3][c] == 1) {
                    return -500000;
                } else if(board[r][c] == 2 && board[r - 1][c] == 2 && board[r - 2][c] == 2 && board[r - 3][c] == 2) {
                    return 500000;
                }
            }
        }

        //check for horizontal win
        for(int r = 0;r < 6;r++) {
            for(int c = 0;c <= 3;c++) {
                if(board[r][c] == 1 && board[r][c + 1] == 1 && board[r][c + 2] == 1 && board[r][c + 3] == 1) {
                    return -500000;
                } else if(board[r][c] == 2 && board[r][c + 1] == 2 && board[r][c + 2] == 2 && board[r][c + 3] == 2) {
                    return 500000;
                }
            }
        }

        //check for diagonal win
        for(int r = 0;r <= 2;r++) {
            for(int c = 0;c < 4;c++) {
                if(board[r][c] == 1 && board[r + 1][c + 1] == 1 && board[r + 2][c + 2] == 1 && board[r + 3][c + 3] == 1) {
                    return -500000;
                } else if(board[r][c] == 2 && board[r + 1][c + 1] == 2 && board[r + 2][c + 2] == 2 && board[r + 3][c + 3] == 2) {
                    return 500000;
                }
            }
        }

        for(int r = 0;r <= 2;r++) {
            for(int c = 6;c >= 3;c--) {
                if(board[r][c] == 1 && board[r + 1][c - 1] == 1 && board[r + 2][c - 2] == 1 && board[r + 3][c - 3] == 1) {
                    return -500000;
                } else if(board[r][c] == 2 && board[r + 1][c - 1] == 2 && board[r + 2][c - 2] == 2 && board[r + 3][c - 3] == 2) {
                    return 500000;
                }
            }
        }

        return 0;
    }
}
