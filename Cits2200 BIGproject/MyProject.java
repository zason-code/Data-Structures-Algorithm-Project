import java.util.*;
/**
 * Write a description of class MyProject here.
 *
 * @author Jason Ho JiaSheng, 22360143
 * @version (a version number or a date)
 */
public class MyProject implements Project
{
    // instance variables - replace the example below with your own
    /**
     * Count number of contiguous pixels that have to change to black starting from row,col.
     *
     * @param image The greyscale image as defined above
     * @param row The row index of the pixel to flood-fill from
     * @param col The column index of the pixel to flood-fill from
     * @return The number of pixels that changed colour when performing this operation
     */
    public int floodFillCount(int[][] image, int row, int col)
    {
        //Queuerow contains row coordinates. Queuecol contains col coordinates.
        int counter=0;
        Queue queuerow = new LinkedList<Integer>();
        Queue queuecol = new LinkedList<Integer>();

        boolean [][]seen = new boolean[image.length][image[0].length];

        queuerow.add(row);
        queuecol.add(col);
        seen[row][col]=true;
        if(image[row][col]==0){
            return counter;
        }

        else {

            seen[row][col]=true;
            counter++;
            while(!(queuerow.isEmpty()) && !(queuecol.isEmpty())) {
                int trow = (int) queuerow.remove();
                int tcol = (int) queuecol.remove();
                int temp = image[trow][tcol];

                //Checking right,left,bottom & top neighbours of the pixel.

                if((tcol+1)<image[trow].length && temp==image[trow][tcol+1] && !seen[trow][tcol+1]){

                    seen[trow][tcol+1]=true;
                    queuerow.add(trow);
                    queuecol.add(tcol+1);
                    counter++;

                }
                if((tcol-1>=0) && temp==image[trow][tcol-1] && !seen[trow][tcol-1]){

                    seen[trow][tcol-1]=true;
                    queuerow.add(trow);
                    queuecol.add(tcol-1);
                    counter++;

                }
                if((trow+1<image.length) && temp==image[trow+1][tcol] && !seen[trow+1][tcol]){

                    seen[trow+1][tcol]=true;
                    queuerow.add(trow+1);
                    queuecol.add(tcol);
                    counter++;

                }
                if((trow-1>=0) && temp==image[trow-1][tcol] && !seen[trow-1][tcol]){

                    seen[trow-1][tcol]=true;
                    queuerow.add(trow-1);
                    queuecol.add(tcol);
                    counter++;

                }
                //setting to black.
                image[trow][tcol]=0;
            }
            return counter;
        }

    }

    /**
     * Find the brightest k x k square (biggest sum)
     *
     * @param image The greyscale image as defined above
     * @param k the dimension of the squares to consider
     * @return The total brightness of the brightest square
     */
    public int brightestSquare(int[][] image, int k){
        int rowsize = image.length;
        int colsize = image[0].length;
        //Creating the 2d array to store the sum values
        int[][] stripsum = new int[rowsize][colsize]; 

        // max_sum used to store maximum sum of square seen.
        int max_sum = 0; 

        //Do col by col 
        for (int j = 0; j < colsize; j++) { 

            // Calculate sum of first 1 x k rectangle in the col
            int sum = 0; 
            for (int i = 0; i < k; i++){
                sum += image[i][j];
            }
            stripsum[0][j] = sum; 

            // Calculate sum of the remaining rects 
            for (int i = 1; i < rowsize - k + 1; i++) { 
                sum += (image[i + k - 1][j] - image[i - 1][j]); 
                stripsum[i][j] = sum; 
            } 
        } 

        // Finding sum of the sub-squares using stripsum[][] 
        for (int i = 0; i < rowsize - k + 1; i++) { 

            // Calculate sum of first subsquare in this row k x 1.
            int sum = 0; 
            for (int j = 0; j < k; j++){
                sum += stripsum[i][j]; 
            }

            // Need to update max_sum if larger.
            if (sum > max_sum) { 
                max_sum = sum; 

            } 

            //Calculate sum of remaining squares in current row by taking away leftmost 
            //strip in the previous sub-square and adding a new strip 
            //Worst case scenario O(R*C)=O(P) if size of square is 1 becos need to iterate through all pixels.
            //First find the sum through going down the row first each of length k
            //Then find the sum through going to the right of the column each of length k
            for (int j = 1; j < colsize - k + 1; j++) { 
                sum += (stripsum[i][j + k - 1] -stripsum[i][j - 1]); 

                // Update max_sum 
                if (sum > max_sum) { 
                    max_sum = sum; 

                } 
            } 
        } 
        return max_sum;
    } 

    /**
     * Find the minimum maximum value encountered from ur,uc to vr,vc
     *
     * @param image The greyscale image as defined above
     * @param ur The row index of the start pixel for the path
     * @param uc The column index of the start pixel for the path
     * @param vr The row index of the end pixel for the path
     * @param vc The column index of the end pixel for the path
     * @return The minimum brightness of any path from (ur, uc) to (vr, vc)
     */
    public int darkestPath(int[][] image, int ur, int uc, int vr, int vc){

        PriorityQueue<Pointer> pq = new PriorityQueue<Pointer>();
        boolean [][]seen = new boolean[image.length][image[0].length];
        //width 2D array used to keep track of minimum maximum value seen.
        int [][] width = new int[image.length][image[0].length];
        Pointer temp;

        for (int[] row: width){
            Arrays.fill(row, 9999); //setting to infinity, any number above 255.Integer.Max
        }
        width[ur][uc]=image[ur][uc];

        pq.add(new Pointer(ur,uc,width[ur][uc]));
        // Variant of Dijkstra
        while(!(pq.isEmpty())){
            temp=pq.remove();
            int trow= temp.r;
            int tcol= temp.c;

            if(seen[trow][tcol]){
                continue;
            }
            seen[trow][tcol]=true;

            //Checking the "neighbours", right, left, bottom and top of the current pixel.
            if((tcol+1)<image[trow].length && !seen[trow][tcol+1] ){
                //min(...,max(....)) used to find the minimum maximum value.
                int x = Math.min(width[trow][tcol+1],Math.max(width[trow][tcol],image[trow][tcol+1]));
                if(x<width[trow][tcol+1]){
                    //Update the min max value seen.
                    width[trow][tcol+1]=x;
                    pq.add(new Pointer(trow,tcol+1,width[trow][tcol+1]));
                }
            }
            if((tcol-1>=0 && !seen[trow][tcol-1]) ){
                int x= Math.min(width[trow][tcol-1],Math.max(width[trow][tcol],image[trow][tcol-1]));
                if(x<width[trow][tcol-1]){
                    width[trow][tcol-1]=x;
                    pq.add(new Pointer(trow,tcol-1,width[trow][tcol-1]));
                }

            }
            if((trow+1<image.length && !seen[trow+1][tcol]) ){
                int x=Math.min(width[trow+1][tcol],Math.max(width[trow][tcol],image[trow+1][tcol]));
                if(x<width[trow+1][tcol]){
                    width[trow+1][tcol]=x;
                    pq.add(new Pointer(trow+1,tcol,width[trow+1][tcol]));
                }
            }
            if((trow-1>=0 && !seen[trow-1][tcol])){
                int x=Math.min(width[trow-1][tcol],Math.max(width[trow][tcol],image[trow-1][tcol]));
                if(x<width[trow-1][tcol]){
                    width[trow-1][tcol]=x;
                    pq.add(new Pointer(trow-1,tcol,width[trow-1][tcol]));
                }
            }
        }

        return width[vr][vc];
    }
    //inner class
    private class Pointer implements Comparable<Pointer> {
        public int r;
        public int c;
        public int maxbrightness;
        public Pointer(int r,int c,int brightness){
            this.r = r;
            this.c = c;
            this.maxbrightness = brightness;
        }
        //min-heap priority queue is used becos want to find minimum maximum value.
        public int compareTo(Pointer other) {
            int otherbrightness = other.maxbrightness;
            if(maxbrightness< otherbrightness){
                return -1;
            }
            else if(maxbrightness> otherbrightness){
                return 1;
            }
            else return 0;
        }
    }
    /**
     * For each query, find the value of the brightest pixel in the specified row segment.
     *
     * @param image The greyscale image as defined above
     * @param queries The list of query row segments
     * @return The list of brightest pixels for each query row segment
     */
    public int[] brightestPixelsInRowSegments(int[][] image, int[][] queries){
        int[] storage = new int[queries.length];

        //Height of segment tree
        int x = (int) Math.ceil(Math.log(image[0].length) / Math.log(2));

        // Maximum size of segment tree 
        int max_size = 2 * (int) Math.pow(2, x) - 1;

        //Creating 2D segment trees array for each row in image
        int[][]segtree = new int[image.length][max_size];

        for(int i=0; i<image.length;i++){
            constructTree(image,segtree,0,image[0].length-1,i,0);

        }
        //Finding maximum value for each query given.
        int j =0;
        while(j <queries.length){
            int temp=Maxfunc(segtree,0,image[0].length-1,queries[j][1],queries[j][2]-1,queries[j][0],0);
            storage[j]=temp;
            j++;
        }

        return storage;
    }
    //Constructing the segment tree.
    public void constructTree(int [][] image, int [][]segtree, int start, int end, int row, int col){
        if(start==end){
            segtree[row][col]=image[row][start];
            return;
        }
        int mid = getMid(start,end);
        constructTree(image, segtree, start,mid,row,2*col+1);
        constructTree(image,segtree,mid+1,end,row,2*col+2);
        segtree[row][col]= Math.max(segtree[row][2*col+1],segtree[row][2*col+2]);

    }
    //find the middle value.
    public int getMid(int st, int en){ 
        return st + (en - st) / 2; 
    } 
    //Finding the max query. 
    public int Maxfunc(int[][] st, int start, int end,int l, int r, int row,int node){ 

        //If segment of current node in the specific row is completely part/overlapped of given range, 
        // then get the max value of the segment. 
        if (l <= start && r >= end){
            return st[row][node]; 
        }

        //If segment of current node does not 
        //belong to the specified range at all.
        if (end < l || start > r){
            return -1; 
        }

        // If segment of current node is partially overlapped by the given range 
        int mid = getMid(start, end); 
        //have to keep track of current row of the segment/image 
        return Math.max(Maxfunc(st, start, mid, l, r,row, 2 * node + 1), Maxfunc(st, mid + 1, end, l, r,row, 2 * node + 2)); 
    } 
}

