* 芋道 Spring Security 入门：<http://www.iocoder.cn/Spring-Boot/Spring-Security/?yudao>
* Spring Security 基本概念：<http://www.iocoder.cn/Fight/Spring-Security-4-1-0-Basic-concept-description/?yudao>

摘要: 原创出处 my.oschina.net/yumg/blog/675213 「YuMG」欢迎转载，保留摘要，谢谢！

1.概述
1.1 Spring Security 中的基本概念和逻辑
1.2 Spring Security的基本应用方法
2.使用SS提供的Web Filters
2.1 核心Filter
2.2 SecurityContextPersistenceFilter
2.3 UsernamePasswordAuthenticationFilter
2.4. ExceptionTranslationFilter
2.5 FilterSecurityInterceptor
1.概述
1.1 Spring Security 中的基本概念和逻辑
Spring Security （简称SS） 用于解决Java应用中的安全管理控制问题，这其中包含两个关键环节：

认证
授权
“认证”的含义是对动作主体进行标识的过程，其结果是认证成功或者不成功。认证成功后，动作主体就可以继续以经过认证的安全主体身份发起对安全对象的操作。否则，将不可能继续后续操作。

“授权”是在安全主体对安全对象进行操作前的权限检查过程。只有通过该检查，安全主体才能够成功操作安全对象。

Spring Security 围绕以上两个过程提供了基本的技术框架。

在认证环节，SS使用Authentication对象，封装了对安全主体的表示。此Authentication对象将作为SecurityContext中的重要组成部分，而SecurityContext正是作为SS为应用提供安全管理控制的上下文基础。应用怎么使用SecurityContext呢？SS提供了SecurityContextHolder来维护SecurityContext对象，以便应用中可以在一次用户级会话处理过程中随时可以使用安全上下文。用代码来反映以上这句话的意思如下：

Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
if (principal instanceof UserDetails) {
String username = ((UserDetails)principal).getUsername();
} else {
String username = principal.toString();
}
这段代码中出现了要给听关键的东西UserDetails。这个接口可以看作是SS与应用业务中安全主体对象衔接的适配器。SS将会使用UserDetails来构造Authenticaiton，而应用也往往会将UserDetails转型为业务领域中的对应对象。那么接下来的问题是UserDetails是从哪里来的呢？SS中需要UserDetails的地方，都会需要UserDetailsService，这个服务仅有一个接口方法：

UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
总结以上概念，认证的逻辑基本如下：

UserDetailsService提供UserDetails，SS通过认证该信息后将其封装为Authenticaiton，这个Authentication会放在安全控制上下文对象中（SecurityConext），并由SecurityContextHolder进行管理。认证完毕后续的环节如果需要认证信息的话，就直接从SecurityContextHolder中取用即可。

在授权环节的基础是SecurityContextHolder的Authenticasiton除了包含主体信息外，还会包含其关联的权限信息。这些权限信息通常也都是通过UserDetailsService加载而来的。使用authentication.getAuthorities()得到主体现有权限，匹配安全对象所需的访问权限，若满足则给予授权放行，反之与之拒绝。

1.2 Spring Security的基本应用方法
SS为安全管理过程中的关键环节中的关键概念都进行了抽象定义，具体如何运用它，需要了解以下几个方面：

SS面向Java应用领域，提供安全管理；
SS以AOP的方法来实施安全管理；
由于SS解决的问题性质，它更多地体现为一种设计框架，而不是可以在其上可以直接构建业务的应用开发框架。使用SS需要对其一定程度的了解，复用的更多是其设计结构而不是代码黑箱。
这里主要说明在Java应用中进行安全管理的问题。从技术上来讲，Java领域中的可基于AOP进行有效安全访问控制的安全对象可以分为两种：“方法调用”和“Web请求调用”。而SS正是以 “around advice” 对 web request （servlet filter baseed）和 方法调用 （spring aop or aspectj based）来进行安全管理控制的。

这样以来对SS的应用方法就比较容易理解了：

当构建Java Web应用时，通常都会对Web Request进行安全控制。这时应该在servlet filter层面就应该着手来实施应用SS提供的各种部件了。在这么干的同时，也可以继续在业务层面上比如Servlet或者什么框架的action、service中继续进行方法对象的管理控制。
如果不是Web应用场景，不需要而且也没机会去布置servlet filter层面的安全控制部件。直接在方法层面布置实施即可（当然，这里的java方法并不一定就直接是业务方法，通常应该是控制器引导方法之类的东西）。
下文中将围绕Java Web应用继续详细说明SS的应用过程。

2.使用SS提供的Web Filters
如前文所述，构建Java Web应用时使用SS进行Web资源访问控制，应该在servlet filter层面进行相关布置。具体就是在web应用描述符文件中配置一个DelegatingFilterProxy，并且在在spring context中配置SS提供的FilterChainProxy。

web.xml配置内容：

<filter>
    <filter-name>myFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>

<filter-mapping>
    <filter-name>myFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
举例applicationContext.xml配置内容：

<bean id="filterChainProxy" class="org.springframework.security.web.FilterChainProxy">
<constructor-arg>
	<list>
	<sec:filter-chain pattern="/restful/**" filters="
		securityContextPersistenceFilterWithASCFalse,
		basicAuthenticationFilter,
		exceptionTranslationFilter,
		filterSecurityInterceptor" />
	<sec:filter-chain pattern="/**" filters="
		securityContextPersistenceFilterWithASCTrue,
		formLoginFilter,
		exceptionTranslationFilter,
		filterSecurityInterceptor" />
	</list>
</constructor-arg>
</bean>
如此一来SS就可介入servlet filter链过程，开始干活。而具体干活的就是其中的一串串的filters了。那么这些filters都可以有哪些，顺序如何，都是干什么的？如以下说明：

以下这些基本是所有可用的filter，其中有些是选用，有些是必须的，但无论如何它们的出现顺序为以下:

ChannelProcessingFilter, because it might need to redirect to a different protocol
SecurityContextPersistenceFilter, so a SecurityContext can be set up in the SecurityContextHolder at the beginning of a web request, and any changes to the SecurityContext can be copied to the HttpSession when the web request ends (ready for use with the next web request)
ConcurrentSessionFilter, because it uses the SecurityContextHolder functionality and needs to update the SessionRegistry to reflect ongoing requests from the principal
Authentication processing mechanisms - UsernamePasswordAuthenticationFilter, CasAuthenticationFilter, BasicAuthenticationFilter etc - so that the SecurityContextHolder can be modified to contain a valid Authentication request token
The SecurityContextHolderAwareRequestFilter, if you are using it to install a Spring Security aware HttpServletRequestWrapper into your servlet container
The JaasApiIntegrationFilter, if a JaasAuthenticationToken is in the SecurityContextHolder this will process the FilterChain as the Subject in the JaasAuthenticationToken
RememberMeAuthenticationFilter, so that if no earlier authentication processing mechanism updated the SecurityContextHolder, and the request presents a cookie that enables remember-me services to take place, a suitable remembered Authentication object will be put there
AnonymousAuthenticationFilter, so that if no earlier authentication processing mechanism updated the SecurityContextHolder, an anonymous Authentication object will be put there
ExceptionTranslationFilter, to catch any Spring Security exceptions so that either an HTTP error response can be returned or an appropriate AuthenticationEntryPoint can be launched
FilterSecurityInterceptor, to protect web URIs and raise exceptions when access is denied
2.1 核心Filter
以上说明中也可以了解到以下几个filter为必须使用的：

SecurityContextPersistenceFilter
[authentication processing filter] etc. UsernamePasswordAuthenticationFilter
ExceptionTranslationFilter
FilterSecurityInterceptor
以下作分述说明：

2.2 SecurityContextPersistenceFilter
SecurityContextPersistenceFilter的作用在于以下几个方面（步骤）：

从SecurityContextRepository中加载或者创建SecurityContext对象,并使用SecurityContextHolder来维护SecurityContext，这个Holder将缺省以线程变量机制为后续部件调用中提供对SecurityContext的访问；

整个请求完成后清理SecurityContextHolder（这里如果使用了ThreadLocal变量来维护安全上下文信息，再加上servlet容器中的线程池设置，有可能会使得线程变量的使用引发混乱，如果不清理的话）；

整个请求完成后，使用SecurityContextRepository将SecurityContext内容的持久化以使得多次HTTP请求之间可以维护同一个安全上下文状态。

Populates the SecurityContextHolder with information obtained from the configured SecurityContextRepository prior to the request and stores it back in the repository once the request has completed and clearing the context holder. By default it uses an HttpSessionSecurityContextRepository. See this class for information HttpSession related configuration options.

This filter will only execute once per request, to resolve servlet container (specifically Weblogic) incompatibilities.

This filter MUST be executed BEFORE any authentication processing mechanisms. Authentication processing mechanisms (e.g. BASIC, CAS processing filters etc) expect the SecurityContextHolder to contain a valid SecurityContext by the time they execute.

This is essentially a refactoring of the old HttpSessionContextIntegrationFilter to delegate the storage issues to a separate strategy, allowing for more customization in the way the security context is maintained between requests.

The forceEagerSessionCreation property can be used to ensure that a session is always available before the filter chain executes (the default is false, as this is resource intensive and not recommended).

总结以上出现的部件概念，如下：

SecurityContextRepository：负责保证在一个用户级会话的过程中，维护住相关的安全上下文信息。这个问题主要发生在web应用中，由于http的无状态性，所以每次request都需要重复加载安全上下文信息。默认情况下，将基于HTTP Session来完成这方面的工作。
SecurityContext：安全上下文信息。
SecurityContextHolder：维护SecurityContext，用户请求处理过程中的各种程序调用能够使用到SecurityContext。默认使用线程变量机制，对于Web应用比较合适。如果是swing程序等，可以换用文件机制。
2.3 UsernamePasswordAuthenticationFilter
For historical reasons, prior to Spring Security 3.0, this filter was called AuthenticationProcessingFilter and the entry
point was called AuthenticationProcessingFilterEntryPoint. Since the framework now supports many different forms
of authentication, they have both been given more specific names in 3.0.
提供认证机制，服务于用户认证。具体的说，UsernamePasswordAuthenticationFilter提供了基于web form提交参数的认证机制。默认情况下，它处理form表单提交来的username与password两个参数。

Processes an authentication form submission. Called AuthenticationProcessingFilter prior to Spring Security 3.0.

Login forms must present two parameters to this filter: a username and password. The default parameter names to use are contained in the static fields SPRING_SECURITY_FORM_USERNAME_KEY and SPRING_SECURITY_FORM_PASSWORD_KEY. The parameter names can also be changed by setting the usernameParameter and passwordParameter properties.

This filter by default responds to the URL /login.
就像其文档中所述，Spring Security提供了多种认证机制，UsernamePasswordAuthenticationFilter只是其中的一种。在代码结构中，它是AbstractAuthenticationProcessingFilter的一种实现。AbstractAuthenticationProcessingFilter是对所有browser-based HTTP-based的认证请求处理的抽象。（事实上它目前仅有UsernamePasswordAuthenticationFilter这么一种实现）。关于AbstractAuthenticationProcessingFilter有以下说明：

AbstractAuthenticationProcessingFilter: Abstract processor of browser-based HTTP-based authentication requests.

Authentication Process:

The filter requires that you set the authenticationManager property. An AuthenticationManager is required to process the authentication request tokens created by implementing classes.
This filter will intercept a request and attempt to perform authentication from that request if the request matches the setRequiresAuthenticationRequestMatcher(RequestMatcher).
Authentication is performed by the attemptAuthentication method, which must be implemented by subclasses.

Authentication Success:

If authentication is successful, the resulting Authentication object will be placed into the SecurityContext for the current thread, which is guaranteed to have already been created by an earlier filter.
The configured AuthenticationSuccessHandler will then be called to take the redirect to the appropriate destination after a successful login. The default behaviour is implemented in a SavedRequestAwareAuthenticationSuccessHandler which will make use of any DefaultSavedRequest set by the ExceptionTranslationFilter and redirect the user to the URL contained therein. Otherwise it will redirect to the webapp root "/". You can customize this behaviour by injecting a differently configured instance of this class, or by using a different implementation.

See the successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication) method for more information.

Authentication Failure:

If authentication fails, it will delegate to the configured AuthenticationFailureHandler to allow the failure information to be conveyed to the client. The default implementation is SimpleUrlAuthenticationFailureHandler , which sends a 401 error code to the client. It may also be configured with a failure URL as an alternative. Again you can inject whatever behaviour you require here.

Event Publication:

If authentication is successful, an InteractiveAuthenticationSuccessEvent will be published via the application context. No events will be published if authentication was unsuccessful, because this would generally be recorded via an AuthenticationManager-specific application event.

Session Authentication:

The class has an optional SessionAuthenticationStrategy which will be invoked immediately after a successful call to attemptAuthentication(). Different implementations can be injected to enable things like session-fixation attack prevention or to control the number of simultaneous sessions a principal may have.
以上说明可以看到，UsernamePasswordAuthenticationFilter只是处理请求认证的外围过程，具体的认证执行是由AuthenticationManager来完成的。也就是说UsernamePasswordAuthenticationFilter必须要赋予一个AuthenticationManager的引用。

详细点说，UsernamePasswordAuthenticationFilter将会从请求中解析出来用户名与密码信息，然后构造出UsernamePasswordAuthenticationToken对象（Authentication接口的一种实现），然后将这个token传递给AuthenticationManager对象去执行认证。一般情况下，我们直接使用Spring Security提供的Provider机制来去做AuthenticationManager这件事情。具体说，使用ProviderManager（AuthenticationManager的一种实现）来选择一种AuthenticationProvider去做authentication。那其实，这时认证的责任就转嫁委托给了一种叫做AuthenticationProvider的接口。AuthenticationProvider接口有很多种实现（这也是做provide模式的目的，意思就是可以同时又存在多种认证机制，有一个对的就ok了）。拿出其中一个provider举例子，比如DaoAuthenticationProvider,虽然它名字里有个Dao，但其实它并不直接进行数据存取，它委托其中的UserDetailsService的属性来执行真正的DAO操作。虽然，Spring Security也提供了很多种关于UserDetailsService的实现，比如JdbcDaoImpl但是通常我们都会自己去实现这个东东了。

UsernamePasswordAuthenticationFilter（解析u&p，构造authToken）--> ProviderManager（找到合适的provider，比如DaoAuthenticationProvider）--> DaoAuthenticationProvider（委托UserdetailService加载用户信息进行检查，确定authentication是否成功）。

2.4. ExceptionTranslationFilter
这个filter是专门用来处理异常对象的，说得具体点是专门为了FilterSecurityInterceptor（位于它身后的一个filter）进行异常对象处理的。它自身不进行安全相关的处理，只负责解决异常时对外提供合适的HTTP响应。 它需要两个重要的属性配置，分别为AuthenticationEntryPoint对象和AccessDeniedHandler。

2.4.1 AuthenticationEntryPoint
它作用于当用户请求访问了一个安全资源，但是却尚未经过认证的时候。这时，位置位于ExceptionTranslationFilter身后的FilterSecurityInterceptor会抛出AuthenticationException或者AccessDeniedException类型的异常对象，这种异常情况将会被AuthenticationEntryPoint的commence所方法处理，然后给予用户一个合适的响应，使得可以接着开启认证过程。比如LoginUrlAuthenticationEntryPoint（AuthenticationEntryPoint的一种具体实现）的响应就会将用户请求重定向到一个登录页，提示用户进行登录操作。

2.4.2 AccessDeniedHandler
如果ExceptionTranslationFilter捕获到了AccessDeniedException，并且经过检查其实相关用户是已经过认证的了，那么这种情况意味着用户对其所尝试的操作不具备足够的权限。这种情况下，ExceptionTranslationFilter会触发AccessDeniedHandler。

缺省情况下会使用AccessDeniedHandlerImpl，会给客户端返回403响应。当然，这里也可以定制化一个处理器，可以将为用户响应重定向到一个更友好，信息更详细的访问拒绝的提示页面。

2.5 FilterSecurityInterceptor
FilterSecurityInterceptor是真正直接负责处理关于HTTP资源的安全保护问题的部件，具体说就是处理安全授权问题的部件。其主要过程为：拦截Authentication对"secure object"的访问，依据“安全元信息属性”列表来决定Authentication对象是否可以访问"secure object"。

对于Spring Secuirty而言，“security object”通常只有两大类东西： “方法调用”和“Web请求”。对这两种东西的安全控制都是通过AOP的方法来实施的，只不过是具体技术略有差别，这种手法从代码结构中的体现如下：

AbstractSecurityInterceptor

|------FilterSecurityInterceptor

|------MethodSecurityInterceptor （AOP Alliance based method invocations）

            |---------AspectJMethodSecurityInterceptor （AspectJ based method invocations）
其中AbstractSecurityInterceptor中已经规划了安全控制过程的抽象，FilterSecurityInterceptor是针对web request的基于filter技术实现，MethodSecurityInterceptor和AspectJMethodSecurityInterceptor是针对method invocation的基于两种java aop技术实现。

AbstractSecurityInterceptor中的抽象过程基本如下：

寻找关联当前“secure object”的配置属性（“configuration attributes”）
把authentication\secure object\configuration attributes提交给AccessDecisionManager（当执行到SecurityInterceptor环节时，SecurityContextHolder中会有一个合法的Authentication对象）
可以在此机会对Authentication对象进行变更
如果授权成功，则允许请求\调用继续往下进行
如果配置了AfterInvocaitonManager，那么就调用该管理器的相关操作
什么是所谓的“configuration attributes”？

“configuration attributes”可以被视为对AbstractSecurityInterceptor而言的一种含有特殊意义的字符串。在程序里对应的接口为ConfigAttribute。这个字符串可以是角色名称或者更复杂的东西，这取决于具体的AccessDecisionManager需要的有多复杂。或者说它是被AccessDecisionManager来解释的。

AccessDecisionManager是个基本接口，其核心方法为：

void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException;
Spring Security为此接口提供了一个抽象实现AbstractAccessDecisionManager，这个抽象实现面向了一种vote机制。具体说，就是AbstractAccessDecisionManager中维护了一个vote器列表：List<AccessDecisionVoter<? extends Object>> decisionVoters。这些vote器接口方法为：

int vote(Authentication authentication, S object, Collection<ConfigAttribute> attributes);
这个vote方法可以有三种返回值：

int ACCESS_GRANTED = 1; //授权
int ACCESS_ABSTAIN = 0; //弃权
int ACCESS_DENIED = -1; //拒绝
那么这样以来，AbstractAccessDecisionManager的子类就专注于一件事情：从它的vote器们的vote结果中决定，是否最终给予授权。不同的子类实现了不同的决策策略：

AffirmativeBased：任何一个vote器给予ACCESS_GRANTED，那么最终结果就给予授权；否则不授权；
ConsensusBased：按少数服从多数策略，给予授权决定；
UnanimousBased：一Piao否决制。