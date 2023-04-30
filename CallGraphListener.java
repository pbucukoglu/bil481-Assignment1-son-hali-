import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.*;

public class CallGraphListener extends Java8BaseListener {
    // Define private static variables
    private static String currentClass = "";
    private static String currentMethod = "";
    private static Map<String, List<String>> callGraph;
    private static List<String> temporaryMethods;

    // Define constructor for CallGraphListener class
public CallGraphListener() {
    // Initialize the callGraph and temporaryMethods as HashMap and ArrayList respectively
    callGraph = new HashMap<>();
    temporaryMethods = new ArrayList<>();
}

    // Override method for entering a method declaration
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        // Clear the temporaryMethods list
        temporaryMethods.clear();
        // Set the currentMethod as the method name
        currentMethod = ctx.methodHeader().methodDeclarator().Identifier().getText();
        // Set the methodName as the concatenation of currentClass and currentMethod
        String methodName = currentClass + "/"+ currentMethod;
        // If the callGraph doesn't contain the methodName, add it with an empty ArrayList as its value
        callGraph.putIfAbsent(methodName, new ArrayList<>());
    }

    @Override
public void enterMethodInvocation(Java8Parser.MethodInvocationContext ctx) {
    // Check if the method invocation has an identifier
    if (ctx.Identifier() != null) {
        // Get the method name from the identifier
        String methodName = ctx.Identifier().getText();
        String className = currentClass;

        // Check if the method invocation has an expressionName
        if (ctx.expressionName() != null) {
            // Get the className from the expressionName
            className = ctx.expressionName().getText();
            // If the typeName is not null, add it to the className
            if (ctx.typeName() != null) {
                className = ctx.typeName().getText() + "." + className;
            }
        // If the method invocation doesn't have an expressionName but has a typeName, get the typeName
        } else if (ctx.typeName() != null) {
            className = ctx.typeName().getText();
        }
        // Set the calledMethod as the concatenation of className and methodName
        String calledMethod = className + "/"+ methodName;
        // Set the callingMethod as the concatenation of currentClass and currentMethod
        String callingMethod = currentClass + "/" + currentMethod;

        // If the callGraph doesn't contain the calledMethod, add it to the temporaryMethods list
        if (!callGraph.containsKey(calledMethod)) {
            temporaryMethods.add(calledMethod);
        }

        // If the callGraph doesn't contain the callingMethod, add it with an empty ArrayList as its value
        callGraph.putIfAbsent(callingMethod, new ArrayList<>());
        // Add the calledMethod to the value list of the callingMethod
        callGraph.get(callingMethod).add(calledMethod);
    }
}

    // Method for exiting a class declaration
public void exitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
    currentClass = "";
}

// Override method for entering a normal class declaration
@Override
public void enterNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
    // Set the currentClass as the class name
    currentClass = ctx.Identifier().getText();
}

    public static Map<String, List<String>> updateGraph(Map<String, List<String>> callGraph) {
        Map<String, List<String>> updatedGraph = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : callGraph.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            List<String> updatedValue = new ArrayList<>(value); // Add the node itself to the list

            Set<String> visited = new HashSet<>(); // Keep track of visited nodes to avoid cycles

            for (String subKey : value) {
                visited.add(subKey);

                List<String> subValue = callGraph.get(subKey);
                if (subValue != null && subValue.size() == 1 && !visited.contains(subValue.get(0))) {
                    updatedValue.add(subValue.get(0));
                    visited.add(subValue.get(0));
                }
            }

            updatedGraph.put(key, updatedValue);
        }

        return updatedGraph;
    }


public static void main(String[] args) throws IOException {
    ANTLRInputStream input = new ANTLRInputStream(System.in);
    Java8Lexer lexer = new Java8Lexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    Java8Parser parser = new Java8Parser(tokens);
    ParseTree tree = parser.compilationUnit();
    ParseTreeWalker walker = new ParseTreeWalker();
    CallGraphListener listener = new CallGraphListener();
    walker.walk(listener, tree);
    System.out.println("parametre: " +callGraph.entrySet());
    StringBuilder buf = new StringBuilder();
    Map<String, List<String>> new_graph = updateGraph(callGraph);
    System.out.println("Çıktı: " + new_graph);
    buf.append("digraph G {\n");

    for (Map.Entry<String, List<String>> entry : new_graph.entrySet()) {
    String callingMethod = entry.getKey();
    String color = entry.getValue().isEmpty() ? "white" : "green";
    String label = callingMethod;
    if (entry.getValue().isEmpty() || (entry.getValue().size() == 1 && entry.getValue().get(0).equals(callingMethod))) {
        color = "white";
        label = callingMethod;
    }
    buf.append("\t\"" + callingMethod + "\" [label=\"" + label + "\", fontcolor=black, color=" + color + ", style=filled];\n");
    for (String calledMethod : entry.getValue()) {
        buf.append("\t\"" + callingMethod + "\" -> \"" + calledMethod + "\";\n");
    }
}



    buf.append("}\n");
    try {
            FileWriter writer = new FileWriter("callgraph.dot");
            writer.write(buf.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    System.out.println(buf.toString());
    

    callGraph.clear();
}


}

