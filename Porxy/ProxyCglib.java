package proxy;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyCglib<T, R> {
	
	private Object target;//目标对象
	private Consumer<T> preProxy;//前置操作（有参数，无返回值）
	private Supplier<R> preProxyRev;//前置操作（无参数，有返回值）
	private Function<T, R> preProxyPramRev;//前置操作（有参数，有返回值）
	private Consumer<T> afteProxy;//后置操作（有参数，无返回值）
	private Function<T, R> afteProxyPramRev;//后置操作（有参数，有返回值）
	
	/**
	 * 公共属性（数据集）
	 */
	public R data;
	/**
	 * 目标对象方法返回的结果
	 */
	public Object targetMR;
	
	@SuppressWarnings("unchecked")
	ProxyCglib(Builder builder){
		this.target = builder.target;
		this.preProxy = (Consumer<T>) builder.preProxy;
		this.preProxyRev = (Supplier<R>) builder.preProxyRev;
		this.preProxyPramRev = (Function<T, R>) builder.preProxyPramRev;
		this.afteProxy = (Consumer<T>) builder.afteProxy;
		this.afteProxyPramRev = (Function<T, R>) builder.afteProxyPramRev;
	}
	
	/**获取代理对象
	 * @return
	 */
	public Object getProxyInstance() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(getMethodInterceptor());
		return enhancer.create();
	}
	
	public MethodInterceptor getMethodInterceptor() {
		return new MethodInterceptor() {
			
			@Override
			public Object intercept(Object sub, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
				exePreProxy(preProxy);
				exePreProxy(preProxyRev);
				exePreProxy(preProxyPramRev);
				Object invokeSuper = methodProxy.invokeSuper(sub, objects);
				targetMR = invokeSuper;
				// 自定义后置方法
				exeAfteProxy(afteProxy);
				if(invokeSuper!=null) {
					// 可对结果进行处理的后置方法
					R exeAfteProxy = exeAfteProxy(afteProxyPramRev);
					if(exeAfteProxy != null)
						return exeAfteProxy;
				}
				return invokeSuper;
			}
		};
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
	@SuppressWarnings("unchecked")
	private R exeAfteProxy(Function<T, R> function) {
		if (function == null)
			throw new IllegalArgumentException("Function must be non-null and of equal length");
		return function.apply((T)this);
	}

	/**
	 * @return代理对象构造器
	 */
	public static Builder newBuilder() {
		return new Builder();
	}
	
	/**自定义链式操作构造器
	 * @author ThinkPad
	 *
	 */
	public static final class Builder{
		private Object target;//目标对象
		private Consumer<?> preProxy;//前置操作（有参数，无返回值）
		private Supplier<?> preProxyRev;//前置操作（无参数，有返回值）
		private Function<?, ?> preProxyPramRev;//前置操作（有参数，有返回值）
		private Consumer<?> afteProxy;//后置操作
		private Function<?, ?> afteProxyPramRev;//后置操作（有参数，有返回值）
		
		private Builder(){
		}
		
		/**
		 * @return--代理对象
		 */
		@SuppressWarnings("rawtypes")
		public Object build() {
			return new ProxyCglib(this).getProxyInstance();
		}
		
		public Builder setTarget(Object target) {
			this.target = target;
			return this;
		}
		public Builder setPreProxy(Consumer<?> preProxy) {
			this.preProxy = preProxy;
			return this;
		}
		public Builder setPreProxy(Supplier<?> preProxyRev) {
			this.preProxyRev = preProxyRev;
			return this;
		}
		public Builder setPreProxy(Function<?, ?> preProxyPramRev) {
			this.preProxyPramRev = preProxyPramRev;
			return this;
		}
		public Builder setAfteProxy(Consumer<?> afteProxy) {
			this.afteProxy = afteProxy;
			return this;
		}

		public Builder setAfteProxy(Function<?, ?> afteProxyPramRev) {
			this.afteProxyPramRev = afteProxyPramRev;
			return this;
		}
	}
	
	public static void main(String[] args) {
		// 目标对象（不需要有接口实现）
		CglibTarget target = new CglibTarget();
		CglibTarget proxy = (CglibTarget) ProxyCglib.newBuilder() 
				.setTarget(target)
				.setPreProxy(t-> System.out.println("pre"))
				.setAfteProxy(t -> System.out.println("afte"))
				.setAfteProxy(t->{// 对方法结果进行处理
					ProxyCglib<?,?> tt = (ProxyCglib<?,?>)t;
					Integer methodres = (Integer) tt.targetMR;
					return methodres + 1;
				})
				.build();
		proxy.save();
		Integer add = proxy.add();
		System.out.println(add);
	}
//	执行结果：
//	pre
//	save...
//	afte
//	
//	pre
//	afte
//	2
}
