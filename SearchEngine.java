import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    LinkedList<String> list = new LinkedList<String>();

    public String handleRequest(URI url) {

        if (url.getPath().equals("/")) {
            return String.format("Welcome to Milana's search engine!");
        } else if (url.getPath().contains("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {

                String toReturn = "";
                int len = list.size();
                String search = parameters[1];

                for (int i = 0; i < len; i++) {
                    String currentWord = list.get(i);
                    boolean hasWord = this.containsSubstring(currentWord, search);
                    if (hasWord) {
                        toReturn += (currentWord + "\n");
                    }
                }
                return String.format(toReturn);
            }
        } else if (url.getPath().contains("/add")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                list.add(parameters[1]);
                return String.format("%s was added to the list!! List is now %s", parameters[1], list);
            }
        }
        
        return "404 Not Found!";
    }

    private boolean containsSubstring(String word, String search) {
        int lenWord = word.length();
        int lenSearch = search.length();
        boolean hasIt = false;
        for (int i = 0 ; i <= lenWord - lenSearch; i++) {
            String sub = word.substring(i, i + lenSearch);
            
            if (sub.equals(search)) {
                hasIt = true;
            }
        }
        return hasIt;
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}