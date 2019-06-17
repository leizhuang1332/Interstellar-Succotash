package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;

public class ProxyMy<T> {

	private Object target;
	private Consumer<T> preProxy;
	private Consumer<T> afteProxy;

	ProxyMy(Object target) {
		this.target = target;
	}
	
	@SuppressWarnings("unchecked")
	ProxyMy(Builder builder) {
		this.target = builder.target;
		this.preProxy = (Consumer<T>) builder.preProxy;
		this.afteProxy = (Consumer<T>) builder.afteProxy;
	}

	public static Builder newBuilder() {
		return new Builder();
	}
	
	public Object getProxyInstance() {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						exePreProxy(preProxy);
						Object invoke = method.invoke(target, args);
						exeAfteProxy(afteProxy);
						return invoke;
					}
				});
	}
	
	@SuppressWarnings("unchecked")
	public void exePreProxy(Consumer<T> consumer) {
		if (consumer != null)
			consumer.accept((T)this);
	}
	@SuppressWarnings("unchecked")
	public void exeAfteProxy(Consumer<T> consumer) {
		if (consumer != null)
			consumer.accept((T)this);
	}
	
	public static final class Builder{
		private Object target;
		private Consumer<?> preProxy;
		private Consumer<?> afteProxy;
		
		private Builder(){
		}
		
		@SuppressWarnings("rawtypes")
		public Object build() {
			return new ProxyMy(this).getProxyInstance();
		}
		public Builder setTarget(Object target) {
			this.target = target;
			return this;
		}
		public Builder setPreProxy(Consumer<?> preProxy) {
			this.preProxy = preProxy;
			return this;
		}
		public Builder setAfteProxy(Consumer<?> afteProxy) {
			this.afteProxy = afteProxy;
			return this;
		}
	}
}
