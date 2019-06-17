package proxy;

import java.util.Date;

public class RunTest {

	public static void main(String[] args) {
		
		TestInterface target = new Target();

		TestInterface proxyInstance2 = (TestInterface) ProxyMy.newBuilder()
				.setTarget(target)
				.setPreProxy(t ->{
					System.out.println(new Date().getTime());
				})
				.setAfteProxy(t -> System.out.println("后"))
				.build();
		proxyInstance2.save();
	}
}
