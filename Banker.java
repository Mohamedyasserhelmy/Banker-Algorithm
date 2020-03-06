import java.util.*;

import static java.lang.System.exit;

public class Banker {
    int M, N;                                           // M = # Resources ,, N = # Processes
    int[] available;
    int[][] maximum;
    int[][] allocation;
    int[][] need;
    int[] limit;
    ArrayList<Integer> tracer = new ArrayList<>();

    public Banker()
    {

    }
    boolean biggerEqual(int[] arr1, int[] arr2)
    {
        boolean result = true;
//        if (arr1.length != arr2.length)
//        {
//            result = false;
//        }
        for (int i =0; i < arr1.length; i++) {
            if (arr1[i] >= arr2[i]) {
                result &= true;
            } else if (arr1[i] < arr2[i]){
                result = false;
                break;
            }

        }

        return result;
    }

    public void parse_input(String in)
    {
        in = in.toLowerCase();
        String buffer;
        boolean be = (allocation == null || maximum == null) ? true : false;
        if (be && in.contains("rq"))
        {
            System.out.println("No Allocated Processes To Request !!");
        }
        if (be && in.contains("input"))
        {
            user_input();
        }
        if (be && in.contains("test"))
        {
            test();
        }
        if (!be) {

            if (in.contains("rq")) {
                buffer = in.substring(2, in.length());
                buffer = buffer.trim();
                System.out.println(buffer);
                utility(buffer);
            }
        }
        if (in.equals("quit"))
        {
            exit(0);
        }
    }
    public void add(int[] get, int[] left, int[] right)
    {
        for (int i =0; i < M; i++)
        {
            get[i] = left[i] + right[i];
        }

    }

    public void again(int T)                // Processes Iteration Function
    {
        // Here The for Loop To Check for suitable process to work with available resources

        boolean compare, compare_2;
        for (int k = 0; k < N; k++)
        {
            int[] predictor = new int[M];
            add(predictor, available, allocation[k]);
            compare_2 = this.biggerEqual(limit ,predictor);
            compare = this.biggerEqual(available, need[k]);
            System.out.println(Arrays.toString(predictor));
            System.out.println(Arrays.toString(need[k]));
            System.out.println(compare);
            System.out.println(compare_2);
            if ( (compare  && compare_2)  && !tracer.contains(k))
            {
//                System.out.println(compare);
//                System.out.println(compare_2);
                System.out.println("P "+ k+ " Can Run");
                update(allocation, k);
                //System.out.println(Arrays.toString(predictor));
                //System.out.println(Arrays.toString(available));
                tracer.add(k);
            }
            else if (tracer.contains(k))
            {
                continue;
            }
            else if(T == N-1 && tracer.size() < M)
            {
                System.out.println("P " + k + " deadlocked");
            }
            else
            {
                continue;
            }
            predictor = null;
        }
        System.out.println(Arrays.asList(tracer));

    }
    public void test()                                     // Test Function That initializes all data as Book Example
    {
        N = 5;                      // Processes
        M = 3;                      //Resources
        available = new int[] {3,3,2};                    // SnapShot of System Resources at time slice T0
        maximum = new int [][] {
                {7,5,3},{3,2,2},{9,0,2},{2,2,2},{4,3,3}
        };
        allocation = new int[][] {
                {0,1,0},{2,0,0},{3,0,2},{2,1,1},{0,0,2}
        };
        need = new int [N][M];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j  < M; j++)
            {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }
        limit = new int[]{10, 5, 7};

        int k = 0;
        while (k < N)                          // At least One Process will run in {0,..., N-1} Times
        {
            System.out.println("iteration " + k);
            again(k);
            if (tracer.size() == 5)
            {
                break;
            }
            k++;
        }

    }

    public void user_input(){
        //limit = new int[]{10, 5, 7};                         // For Testing Reasons
        Scanner getIn = new Scanner(System.in);
        System.out.print("Enter Number Of Processes : ");
        N = getIn.nextInt();
        System.out.print("Enter The Number Of Allocated Resources");
        M = getIn.nextInt();
        available = new int[M];
        maximum = new int[N][M];
        allocation = new int[N][M];
        need = new int[N][M];
        limit = new int[M];

        System.out.println("Enter The Resources Limits : ");
        for(int j = 0; j < M; j++)
        {
            limit[j] = getIn.nextInt();
        }
        System.out.print("Enter Available Resources : ");
        for (int i =0; i < M; i ++)
        {
            available[i] = getIn.nextInt();
        }

        System.out.print("Enter The Allocation Matrix :");

        for (int i =0; i < N; i++)
        {
            for (int j = 0; j < M; j++)
            {
                allocation[i][j] = getIn.nextInt();
            }
        }

        System.out.print("Enter Maximum Need Matrix: ");
        for (int k = 0; k< N; k++)
        {
            for (int t= 0; t < M; t++)
            {
                maximum[k][t] = getIn.nextInt();
            }
        }
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j  < M; j++)
            {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }

        System.out.println(Arrays.deepToString(need));
        int c = 0;
        while (c < N)
        {
            System.out.println("iteration " + c);
            again(c);
            if (tracer.size() == 5)
            {
                break;
            }
            c++;
        }

    }

    public void update(int[][] mat, int delimeter)                     // Update The Available Resources
    {
        int[] getter = mat[delimeter];
        for (int i =0; i < M; i++)
        {
            available[i] = available[i] + getter[i];
        }
    }

    public void utility(String input)                   // Utility Function to adjust Request In a Proper Form
    {
        Scanner scn = new Scanner(System.in);
        String adj;
        int pro_num;                                     // Process Number
        int allo_num;                                    // Number Of Allocated Resources
        int New[];
        if (input.charAt(0) == 'p')
        {
            adj = input.replace("p", "");
            adj = adj.replaceAll("\\s", "");
            pro_num =  Character.getNumericValue(adj.charAt(0));
            allo_num = adj.length() - 1;
            System.out.println(pro_num + " " + allo_num);
            if (pro_num < 0 || pro_num > N || allo_num != M)
            {
                System.out.println("Invalid Request !");
            }
            New = new int[allo_num];
            for (int i =0; i < allo_num; i++)
            {
                New[i] = Character.getNumericValue(adj.charAt(i+1));
            }
            allocation[pro_num] = null;
            allocation[pro_num] = new int[pro_num];
            allocation[pro_num] = New;
            System.out.println(Arrays.toString(allocation[pro_num]));
            int c = 0;
            tracer.clear();
            available = null;
            available = new int[allo_num];
            System.out.print("Enter New Available Resources : ");
            for (int i = 0; i < allo_num; i++)
            {
                available[i] = scn.nextInt();
            }
            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j  < M; j++)
                {
                    need[i][j] = maximum[i][j] - allocation[i][j];
                }
            }

            while (c < N)
            {
                System.out.println("iteration " + c);
                again(c);
                if (tracer.size() == 5)
                {
                    break;
                }
                c++;
            }
        }
        else {
            System.out.println("Enter A valid Request !");
            //exit(0);
        }
    }

    public static void main(String... args)
    {
        Scanner scn = new Scanner(System.in);
        String in;
        Banker bank = new Banker();
        while(scn.hasNextLine())
        {
            in = scn.nextLine();
            bank.parse_input(in);
        }

//        int[] arr = new int[]{10,7,5};
//        int[] comp = new int[]{9,8,3};
//        boolean n = bank.biggerEqual(arr,comp);
//        System.out.println(n);
    }
}
