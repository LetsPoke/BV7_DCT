package uebung7;

public class DCT {

    private static float MSE = 0;

    public static int n = 8,m = 8;
    public static double pi = 3.142857;

    //public double[][] DCTBlock(Block block)
    //Matrix.= DCTBlock()
    //copy2Raster(RasterImage dst, double[][] matrix, int x, int y) {

    public static float getMSE(){
        return MSE;
    }

    public static void IDCT(double[][] matrix, int x, int y, RasterImage dst){
        double ck= 0, cl = 0, dct1 = 0;

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){

                double sum = 0;
                for (int k = 0; k < m; k++) {
                    for (int l = 0; l < n; l++) {

                        if (k == 0) { ck = 1/ Math.sqrt(m); }
                        else { ck = Math.sqrt(2) / Math.sqrt(m); }

                        if (l == 0) { cl = 1/ Math.sqrt(n); }
                        else { cl = Math.sqrt(2) / Math.sqrt(n); }

//                        int pos = (x+k) +  (y+l)*dst.width;
//                        matrix[k][l] = dst.argb[pos] & 0xff;
                        dct1 = matrix[k][l] * Math.cos((2 * k + 1) * i * pi / (2 * m)) * Math.cos((2 * l + 1) * j * pi / (2 * n));
                        sum = sum + dct1;
                    }
                }
                matrix[x+i][y+j] = ck * cl * sum;
            }
        }
    }

    public static void DCTTransformation(RasterImage src, RasterImage dst, RasterImage reconstructedImage, double[][] matrix){
        for(int y=0; y<src.height; y+=8){
            for(int x=0; x<src.width; x+=8){
                int pos = y * src.width + x;

                DCT(matrix, x ,y , src);
                DCTImage(pos, dst, x, y, matrix);

                IDCT(matrix, x, y, dst);
                reconstructedImage(pos, reconstructedImage, x, y, matrix);

                //MSE = MSE + e * e; //Square and add
            }
        }
        MSE = MSE/src.argb.length; //mean
    }

    public static void DCT(double[][] matrix, int x, int y, RasterImage src){
        // source: https://www.geeksforgeeks.org/discrete-cosine-transform-algorithm-program/
        double[][] dct = new double[m][n];
        double ci, cj, dct1;

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){

                if (i == 0) { ci = 1/ Math.sqrt(m); }
                else { ci = Math.sqrt(2) / Math.sqrt(m); }

                if (j == 0) { cj = 1/ Math.sqrt(n); }
                else { cj = Math.sqrt(2) / Math.sqrt(n); }

                double sum = 0;
                for (int k = 0; k < m; k++) {
                    for (int l = 0; l < n; l++) {
                        int pos = (x+k) + (y+l)*src.width;
                       // matrix[k][l] = src.argb[pos] & 0xff;
                        double G = src.argb[pos] & 0xff;
                        dct1 = G * Math.cos((2 * k + 1) * i * pi / (2 * m)) * Math.cos((2 * l + 1) * j * pi / (2 * n));
                        sum = sum + dct1;
                    }
                }
                matrix[x+i][y+j] = ci * cj * sum;
            }
        }
    }

    public static void DCTImage(int pos, RasterImage dst, int x, int y, double[][] matrix){
        int color = 0;
        if(x %8==0 && y%8 ==0) {
            color = (int) matrix[x][y] / 8;
        }else {
            color = (int) matrix[x][y] / 8 + 128;
        }
        if(color >= 255){ color = 255; }
        if(color <= 0){ color = 0; }

        dst.argb[pos] = 0xff << 24 | color << 16 | color << 8 | color;
    }

    public static void reconstructedImage(int pos, RasterImage reconstructedImage, int x, int y, double[][] matrix){
        int color = (int) matrix[x][y];
        if(color >= 255){ color = 255; }
        if(color <= 0){ color = 0; }

        reconstructedImage.argb[pos] = 0xff << 24 | color << 16 | color << 8 | color;
    }

    public static double entropy(RasterImage image){
        double entropy = 0;
        int[] histogram = new int[256];
        for(int index = 0; index < image.argb.length; index++) {
            histogram[image.argb[index] & 0xff]++;
        }

        for(int index = 0; index < histogram.length; index++) {
            if (histogram[index] != 0) {
                double pj = (double)image.argb.length/histogram[index];
                entropy = entropy + -1 * ((1 / pj) * (((-1) * Math.log(pj)) / Math.log(2)));
            }
        }
        return entropy;
    }
}