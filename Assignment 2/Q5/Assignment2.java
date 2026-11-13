
import java.util.*;
import java.io.*;

public class Assignment2 {
    static Random random = new Random(42);
    static volatile long sink = 0;

    static long timeNanos(Runnable r) {
        long start = System.nanoTime();
        r.run();
        return System.nanoTime() - start;
    }

    // ---------------- Q1: String vs StringBuilder vs StringBuffer ----------------
    static String ticketPlus(int tickets, int fields) {
        String result = "";
        for (int t=0; t<tickets; t++) {
            String ticket = "";
            for (int f=0; f<fields; f++) ticket += "FIELD" + f + "=" + t + ";";
            result = ticket;
        }
        return result;
    }
    static String ticketBuilder(int tickets, int fields) {
        String result = "";
        for (int t=0; t<tickets; t++) {
            StringBuilder b = new StringBuilder();
            for (int f=0; f<fields; f++) b.append("FIELD").append(f).append("=").append(t).append(";");
            result = b.toString();
        }
        return result;
    }
    static String ticketBuffer(int tickets, int fields) {
        String result = "";
        for (int t=0; t<tickets; t++) {
            StringBuffer b = new StringBuffer();
            for (int f=0; f<fields; f++) b.append("FIELD").append(f).append("=").append(t).append(";");
            result = b.toString();
        }
        return result;
    }
    static void q1(PrintWriter out) {
        System.out.println("\\nQ1 — String construction benchmark (nanoseconds; best of 3 after warm-up)");
        out.println("Q1,workload,method,nanos");
        int[][] workloads={{1,10},{1000,50},{3000,100}};
        for (int[] w:workloads) {
            int tickets=w[0], fields=w[1];
            Runnable[] jobs={()->sink=ticketPlus(tickets,fields).length(),
                ()->sink=ticketBuilder(tickets,fields).length(),
                ()->sink=ticketBuffer(tickets,fields).length()};
            String[] names={"StringPlus","StringBuilder","StringBuffer"};
            for(int i=0;i<3;i++) {
                jobs[i].run();
                long best=Long.MAX_VALUE;
                for(int r=0;r<3;r++) best=Math.min(best,timeNanos(jobs[i]));
                System.out.println(tickets+" tickets x "+fields+" fields | "+names[i]+" | "+best);
                out.println("Q1,"+tickets+"x"+fields+","+names[i]+","+best);
            }
        }
    }

    // ---------------- Q2: List and Deque comparisons ----------------
    static void q2(PrintWriter out) {
        System.out.println("\\nQ2 — List operations benchmark (nanoseconds; one operation batch)");
        out.println("Q2,size,structure,operation,nanos");
        int[] sizes={1000,5000,10000};
        for(int n:sizes) {
            ArrayList<Integer> a=new ArrayList<>();
            LinkedList<Integer> l=new LinkedList<>();
            ArrayDeque<Integer> d=new ArrayDeque<>();
            for(int i=0;i<n;i++){a.add(i);l.add(i);d.addLast(i);}
            int reps=Math.min(n,2000);
            long ta=timeNanos(()->{for(int i=0;i<reps;i++)a.add(a.size(),i);});
            long tl=timeNanos(()->{for(int i=0;i<reps;i++)l.addLast(i);});
            out.println("Q2,"+n+",ArrayList,addEnd,"+ta);
            out.println("Q2,"+n+",LinkedList,addEnd,"+tl);
            long af=timeNanos(()->{for(int i=0;i<reps;i++)a.add(0,i);});
            long lf=timeNanos(()->{for(int i=0;i<reps;i++)l.add(0,i);});
            long df=timeNanos(()->{for(int i=0;i<reps;i++)d.addFirst(i);});
            out.println("Q2,"+n+",ArrayList,addFront,"+af);
            out.println("Q2,"+n+",LinkedList,addFront,"+lf);
            out.println("Q2,"+n+",ArrayDeque,addFront,"+df);
            int mid=a.size()/2;
            long am=timeNanos(()->{for(int i=0;i<reps;i++){a.add(a.size()/2,i);a.remove(a.size()/2);}});
            long lm=timeNanos(()->{for(int i=0;i<reps;i++){l.add(l.size()/2,i);l.remove(l.size()/2);}});
            out.println("Q2,"+n+",ArrayList,insertRemoveMiddle,"+am);
            out.println("Q2,"+n+",LinkedList,insertRemoveMiddle,"+lm);
            int idx=a.size()/2;
            long ag=timeNanos(()->{for(int i=0;i<10000;i++)sink+=a.get(idx);});
            long lg=timeNanos(()->{for(int i=0;i<10000;i++)sink+=l.get(idx);});
            out.println("Q2,"+n+",ArrayList,getK,"+ag);
            out.println("Q2,"+n+",LinkedList,getK,"+lg);
            long ai=timeNanos(()->{long s=0;for(int x:a)s+=x;sink=s;});
            long li=timeNanos(()->{long s=0;for(int x:l)s+=x;sink=s;});
            out.println("Q2,"+n+",ArrayList,iterate,"+ai);
            out.println("Q2,"+n+",LinkedList,iterate,"+li);
            System.out.println("Completed size "+n);
        }
        System.out.println("Q2 interpretation: ArrayList suits append/index-heavy playlists; LinkedList suits edits through an existing iterator; ArrayDeque suits double-ended queues.");
    }

    // ---------------- Q3: equals/hashCode contract ----------------
    static class Student {
        final String regNo, name;
        Student(String r,String n){regNo=r;name=n;}
        public boolean equals(Object o){return o instanceof Student && regNo.equals(((Student)o).regNo);}
        // TODO 1 completed: equal registration numbers produce equal hash codes.
        public int hashCode(){return Objects.hash(regNo);}
        public String toString(){return regNo+" "+name;}
    }
    static class BadStudent {
        String regNo;
        BadStudent(String r){regNo=r;}
        public boolean equals(Object o){return o instanceof BadStudent && regNo.equals(((BadStudent)o).regNo);}
        // Deliberately no hashCode override: demonstrates broken contract.
    }
    static void q3() {
        System.out.println("\\nQ3 — HashSet equals/hashCode contract");
        Set<BadStudent> bad=new HashSet<>();
        bad.add(new BadStudent("R1")); bad.add(new BadStudent("R1"));
        System.out.println("Broken class, two equal IDs inserted: set size may be 2 (observed "+bad.size()+")");
        Set<Student> good=new HashSet<>();
        good.add(new Student("R1","Arun")); good.add(new Student("R2","Divya")); good.add(new Student("R1","Arun"));
        System.out.println("Correct class, unique students: "+good.size()+" (expected 2)");
        Set<Student> mutableTest=new HashSet<>();
        Student fixed=new Student("R3","Kavi"); mutableTest.add(fixed);
        System.out.println("Immutable key design: contains after insertion = "+mutableTest.contains(fixed));
        System.out.println("Note: changing a field used by hashCode after insertion can make lookup/removal fail; use immutable key fields.");
        System.out.println("Alternative: TreeSet with comparator on regNo or HashMap keyed by regNo also de-duplicates by ID.");
    }

    // ---------------- Q4: Map comparisons ----------------
    static void q4(PrintWriter out) {
        System.out.println("\\nQ4 — Map benchmark");
        out.println("Q4,size,map,lookup100kNanos,codeReportNanos,insertionReportNanos");
        int[] sizes={1000,5000,10000};
        for(int n:sizes) {
            Map<String,Integer> hash=new HashMap<>(), tree=new TreeMap<>(), linked=new LinkedHashMap<>();
            for(int i=0;i<n;i++){String k=String.format("MED%06d",i);hash.put(k,i);tree.put(k,i);linked.put(k,i);}
            String[] keys=hash.keySet().toArray(new String[0]);
            Map<String,Integer>[] maps=new Map[]{hash,tree,linked};
            String[] names={"HashMap","TreeMap","LinkedHashMap"};
            for(int m=0;m<maps.length;m++){
                Map<String,Integer> map=maps[m];
                long lookup=timeNanos(()->{long s=0;for(int i=0;i<100000;i++)s+=map.get(keys[i%keys.length]);sink=s;});
                long code=timeNanos(()->{List<String> ks=new ArrayList<>(map.keySet());Collections.sort(ks);sink=ks.size();});
                long insertion=timeNanos(()->{long s=0;for(String k:map.keySet())s+=map.get(k);sink=s;});
                out.println("Q4,"+n+","+names[m]+","+lookup+","+code+","+insertion);
            }
            System.out.println("Q4 completed "+n+" medicines. TreeMap key order starts: "+tree.keySet().iterator().next());
        }
        System.out.println("HashMap: average O(1) lookup, no order. TreeMap: O(log n) lookup and sorted keys. LinkedHashMap: average O(1), insertion order.");
    }

    // ---------------- Q5: Generics, Comparator ----------------
    static class Team {
        final String name; final int points, goalDiff;
        Team(String n,int p,int g){name=n;points=p;goalDiff=g;}
        public String toString(){return name+"("+points+","+goalDiff+")";}
    }
    static class Leaderboard<T> {
        private final List<T> items=new ArrayList<>();
        private final Comparator<? super T> order;
        Leaderboard(Comparator<? super T> c){order=c;}
        void add(T item){items.add(item);}
        List<T> ranking(){List<T> copy=new ArrayList<>(items);copy.sort(order);return copy;}
    }
    static void q5() {
        System.out.println("\\nQ5 — Generic leaderboard");
        Comparator<Team> byRank=Comparator.comparingInt((Team t)->t.points).reversed()
            .thenComparing(Comparator.comparingInt((Team t)->t.goalDiff).reversed())
            .thenComparing(t->t.name);
        Leaderboard<Team> board=new Leaderboard<>(byRank);
        board.add(new Team("Panthers",9,4));board.add(new Team("Eagles",9,6));
        board.add(new Team("Cobras",6,1));board.add(new Team("Bisons",9,4));
        System.out.println(board.ranking());
        System.out.println("Bisons is above Panthers because points and goal difference tie, then Bisons is alphabetically first.");
        System.out.println("Comparator contract: compare fields in priority order; never subtract integers (overflow risk); use comparingInt/thenComparing.");
    }

    // ---------------- Q6: Hospital triage ----------------
    static class Patient {
        final String name; final int severity; final long arrival;
        Patient(String n,int s,long a){name=n;severity=s;arrival=a;}
        public String toString(){return name+"(S"+severity+")";}
    }
    static class Triage {
        long counter=0;
        PriorityQueue<Patient> queue=new PriorityQueue<>(
            Comparator.comparingInt((Patient p)->p.severity).thenComparingLong(p->p.arrival));
        void admit(String name,int severity){queue.add(new Patient(name,severity,counter++));}
        Patient next(){return queue.poll();}
    }
    static void q6(PrintWriter out) {
        System.out.println("\\nQ6 — PriorityQueue triage");
        Triage er=new Triage();
        er.admit("Ravi",3);er.admit("Meena",1);er.admit("John",3);
        er.admit("Asha",5);er.admit("Kumar",1);er.admit("Lata",2);
        StringBuilder order=new StringBuilder();Patient p;
        while((p=er.next())!=null)order.append(p).append(' ');
        System.out.println("Treatment order: "+order.toString().trim());
        out.println("Q6,size,addNanos,removeAllNanos");
        for(int n:new int[]{1000,5000,10000}) {
            final int size=n;
            long add=timeNanos(()->{PriorityQueue<Integer> q=new PriorityQueue<>();for(int i=0;i<size;i++)q.add(random.nextInt());sink=q.size();});
            PriorityQueue<Integer> q=new PriorityQueue<>();for(int i=0;i<n;i++)q.add(random.nextInt());
            long remove=timeNanos(()->{while(!q.isEmpty())sink=q.poll();});
            out.println("Q6,"+n+","+add+","+remove);
        }
        System.out.println("Starvation: minor cases can wait if critical cases continually arrive. Use aging: gradually improve effective priority as waiting time grows.");
    }

    // ---------------- Q7: diagnose and redesign ----------------
    static void q7(PrintWriter out) {
        System.out.println("\\nQ7 — Redesign: frequent front insertions");
        System.out.println("Diagnosis: ArrayList shifts approximately n elements for each insertion at index 0.");
        System.out.println("Objective: reduce front-insertion cost by using a deque with amortized constant-time addFirst.");
        out.println("Q7,size,arrayListFrontInsertNanos,arrayDequeFrontInsertNanos");
        for(int n:new int[]{1000,5000,10000,20000}) {
            long a=timeNanos(()->{ArrayList<Integer> list=new ArrayList<>();for(int i=0;i<n;i++)list.add(0,i);sink=list.size();});
            long d=timeNanos(()->{ArrayDeque<Integer> deque=new ArrayDeque<>();for(int i=0;i<n;i++)deque.addFirst(i);sink=deque.size();});
            out.println("Q7,"+n+","+a+","+d);
            System.out.println("n="+n+" ArrayList="+a+" ns; ArrayDeque="+d+" ns");
        }
        System.out.println("Trade-off: ArrayDeque has no indexed get; ArrayList remains preferable for random access and mostly append operations.");
    }

    public static void main(String[] args) throws Exception {
        try(PrintWriter out=new PrintWriter(new FileWriter("q1_measurements.csv"))) { q1(out); }
        try(PrintWriter out=new PrintWriter(new FileWriter("q2_measurements.csv"))) { q2(out); }
        try(PrintWriter out=new PrintWriter(new FileWriter("q4_measurements.csv"))) { q4(out); }
        try(PrintWriter out=new PrintWriter(new FileWriter("q6_measurements.csv"))) { q6(out); }
        try(PrintWriter out=new PrintWriter(new FileWriter("q7_measurements.csv"))) { q7(out); }
        q3(); q5();
        System.out.println("\\nRaw measurements saved as q1_measurements.csv through q7_measurements.csv");
    }
}
