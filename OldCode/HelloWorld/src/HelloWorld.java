
public class HelloWorld {
	
	public static int add(int A, int B) {
		return A + B;
	}

	public static void main(String[] args) {
		boolean debug = true;
		int testInteger = add(5, 5);
		if (debug == true) {
			System.out.println("Hello, World!");
			System.out.println(testInteger);
		}
	}

}
