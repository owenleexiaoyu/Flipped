package life.lixiaoyu.flipped.net.http

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Path(
    val value: String,
    val encoded: Boolean = false
)
