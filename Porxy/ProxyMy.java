package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ProxyMy<T, R> {

	private Object target;//目标对象
	private Consumer<T> preProxy;//前置操作（有参数，无返回值）
	private Supplier<R> preProxyRev;//前置操作（无参数，有返回值）
	private Function<T, R> preProxyPramRev;//前置操作（有参数，有返回值）
	private Consumer<T> afteProxy;//后置操作
	
	//运行时被代理对象信息
	private Object proxy;
	private Method method;
	private Object[] args;
	
	/**
	 * 公共属性（数据集）
	 */
	public R data;

	ProxyMy(Object target) {
		this.target = target;
	}
	
	@SuppressWarnings("unchecked")
	ProxyMy(Builder builder) {
		this.target = builder.target;
		this.preProxy = (Consumer<T>) builder.preProxy;
		this.preProxyRev = (Supplier<R>) builder.preProxyRev;
		this.preProxyPramRev = (Function<T, R>) builder.preProxyPramRev;
		this.afteProxy = (Consumer<T>) builder.afteProxy;
	}

	/**
	 * @return代理对象构造器
	 */
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public Object getProxyInstance() {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						setBeProxyObj(proxy, method, args);
						// 自定义前置方法
						exePreProxy(preProxy);
						exePreProxy(preProxyRev);
						exePreProxy(preProxyPramRev);
						Object invoke = method.invoke(target, args);
						Thread.sleep(500);
						// 自定义后置方法
						exeAfteProxy(afteProxy);
						return invoke;
					}
				});
	}
	
	/**设置被代理对象信息
	 * @param proxy
	 * @param method
	 * @param args
	 */
	private void setBeProxyObj(Object proxy, Method method, Object[] args) {
		this.proxy = proxy;
		this.method = method;
		this.args = args;
	}
	
	@SuppressWarnings("unchecked")
	private void exePreProxy(Consumer<T> consumer) {
		if (consumer != null)
			consumer.accept((T)this);
	}
	private void exePreProxy(Supplier<R> supplier) {
		if(supplier != null)
			this.data = supplier.get();
	}
	@SuppressWarnings("unchecked")
	private void exePreProxy(Function<T, R> function) {
		if(function != null) {
			this.data = function.apply((T)this);
		}
	}
	@SuppressWarnings("unchecked")
	private void exeAfteProxy(Consumer<T> consumer) {
		if (consumer != null)
			consumer.accept((T)this);
	}
	
	public Object getProxy() {
		return proxy;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}

	public R getData() {
		return data;
	}

	/**自定义链式操作构造器
	 * @author ThinkPad
	 *
	 */
	public static final class Builder{
		private Object target;
		private Consumer<?> preProxy;
		private Supplier<?> preProxyRev;
		private Function<?,?> preProxyPramRev;
		private Consumer<?> afteProxy;

		private Builder(){
		}
		
		/**
		 * @return--代理对象
		 */
		@SuppressWarnings("rawtypes")
		public Object build() {
			return new ProxyMy(this).getProxyInstance();
		}
		/**被代理的目标对象
		 * @param target
		 * @return
		 */
		public Builder setTarget(Object target) {
			this.target = target;
			return this;
		}
		/**代理前置操作
		 * @param preProxy
		 * @return
		 */
		public Builder setPreProxy(Consumer<?> preProxy) {
			this.preProxy = preProxy;
			return this;
		}
		/**代理前置操作
		 * @param preProxyRev
		 * @return
		 */
		public Builder setPreProxy(Supplier<?> preProxyRev) {
			this.preProxyRev = preProxyRev;
			return this;
		}
		/**代理前置操作
		 * @param preProxyRev
		 * @return
		 */
		public Builder setPreProxy(Function<?, ?> preProxyPramRev) {
			this.preProxyPramRev = preProxyPramRev;
			return this;
		}
		/**代理后置操作
		 * @param afteProxy
		 * @return
		 */
		public Builder setAfteProxy(Consumer<?> afteProxy) {
			this.afteProxy = afteProxy;
			return this;
		}
	}
	
	public static void main(String[] args) {

		//目标对象（需要有接口实现）
		TestInterface target = new Target();

		TestInterface proxyInstance2 = (TestInterface) ProxyMy.newBuilder()
				.setTarget(target)
				.setPreProxy(t -> System.out.println("前置操作有参无返回值"))
				.setPreProxy(() -> {System.out.println("前置操作无参有返回值");return new Date().getTime();})
				.setPreProxy(t -> {System.out.println("前置操作有参有返回值");return new Date().getTime();})
				.setAfteProxy(t -> {
					ProxyMy<?,?> tt = (ProxyMy<?,?>)t;
					long start = (long) tt.data;
					long end = new Date().getTime();
					System.out.println(tt.getMethod().getName()+"方法耗时" + (end - start) + "ms");
				}).build();
		proxyInstance2.save();

//		执行结果：
//		前置操作有参无返回值
//		前置操作无参有返回值
//		前置操作有参有返回值
//		Target is save
//		save方法耗时500ms

	}
}
