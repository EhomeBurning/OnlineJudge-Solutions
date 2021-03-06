/*
### Corner cases
1. Input is empty
2. There is no valid order

### Solution
1. Toposort: abstract the relationships from input, use toposort to construct the order string
    - Time complexity: O(sl)
    - Space complexity: O(1) (26 * 26)
    
### Bugs
1. RE will happen if trying to remove key when visiting keySet

### Test cases


*/
class Solution {
    
    private void buildRelation(String a, String b, Map<Character, Set<Character>> pre) {
        int i = 0, j = 0;
        while (i < a.length() && j < b.length() && 
               a.charAt(i) == b.charAt(j)) {
            i ++;
            j ++;
        }
        if (i == a.length()) return;
        pre.get(b.charAt(j)).add(a.charAt(i));
    }
    
    public String alienOrder(String[] words) {
        Map<Character, Set<Character>> pre = new HashMap<Character, Set<Character>>();
        
        // prepare pre map and calculate how many different characters are there
        for (String word : words) {
            for (char ch : word.toCharArray())
                if (!pre.containsKey(ch))
                    pre.put(ch, new HashSet<Character>());
        }
        int num = pre.size();
        
        // build letter relations from input
        for (int i = 0; i < words.length - 1; i ++) {
            buildRelation(words[i], words[i + 1], pre);
        }
        
        // Toposort
        StringBuilder ans = new StringBuilder();
        Queue<Character> queue = new LinkedList<Character>();
        for (char ch : pre.keySet()) 
            if (pre.get(ch).isEmpty()) {
                queue.add(ch);
            }
        while (!queue.isEmpty()) {
            char cur = queue.poll();
            ans.append(cur);
            for (char ch : pre.keySet()) {
                Set<Character> set = pre.get(ch);
                if (set.contains(cur)) {
                    set.remove(cur);
                    if (set.isEmpty()) {
                        queue.add(ch);
                    }
                }
            }
        }
        if (ans.length() < num) return "";
        else return ans.toString();
    }
}

// new toposort
class Solution {
    public String alienOrder(String[] words) {
        Map<Character, Integer> degree = new HashMap<>();
        Map<Character, Set<Character>> graph = new HashMap<>();
        for (String word : words)
            for (char ch : word.toCharArray())
                if (!degree.containsKey(ch)) {
                    degree.put(ch, 0);
                    graph.put(ch, new HashSet<>());
                }
        
        for (int i = 0; i < words.length - 1; i ++) {
            String prev = words[i], next = words[i + 1];
            if (prev.length() > next.length() && prev.startsWith(next)) return "";
            for (int j = 0; j < Math.min(prev.length(), next.length()); j ++) {
                char cp = prev.charAt(j), cn = next.charAt(j);
                if (cp != cn) {
                    if (!graph.get(cp).contains(cn)) {
                        graph.get(cp).add(cn);
                        degree.put(cn, degree.get(cn) + 1); 
                    }
                    break;
                }
            }
        }
        
        Queue<Character> queue = new LinkedList<>();
        for (char ch : degree.keySet())
            if (degree.get(ch) == 0)
                queue.add(ch);
        StringBuilder ans = new StringBuilder();
        while (!queue.isEmpty()) {
            char cur = queue.poll();
            ans.append(cur);
            Set<Character> nbs = graph.get(cur);
            for (char nb : nbs) {
                degree.put(nb, degree.get(nb) - 1);
                if (degree.get(nb) == 0)
                    queue.add(nb);
            }
        }
            
        if (ans.length() != degree.size()) return "";
        return ans.toString();
    }
}