package rankednetwork.NetworkLevelling;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation provides metadata for boosters.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BoosterMetadata {

	/**
	 * Defines the name of the statistic the booster is for.
	 * The default value is "null".
	 *
	 * @return the name of the booster
	 */
	String name() default "null";

	/**
	 * Defines the visibility of the booster.
	 * The default value is "public".
	 *
	 * @return the visibility of the booster
	 */
	String visibility() default "public";
}
