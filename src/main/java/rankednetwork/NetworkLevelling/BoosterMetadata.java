package rankednetwork.NetworkLevelling;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BoosterMetadata {

	String name() default "null";
	String visibility() default "public";
}
