public class Main {

    public static void main(String[] args) {

        Sandbox sandbox = new Sandbox();

        new UI(
                Sandbox.WIDTH,
                Sandbox.HEIGHT,
                sandbox);
    }
}