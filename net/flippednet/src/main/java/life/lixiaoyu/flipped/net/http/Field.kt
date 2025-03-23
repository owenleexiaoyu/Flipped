package life.lixiaoyu.flipped.net.http

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(
    val value: String,
    val encoded: Boolean = false
)
