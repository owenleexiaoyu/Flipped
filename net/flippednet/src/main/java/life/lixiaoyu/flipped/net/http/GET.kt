package life.lixiaoyu.flipped.net.http

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(
    val value: String = ""
)
