
package com.kileyowen.degrees_of_separation.wikipedia;

public class WikiResult {

	/**
	 * Root Level
	 * Marks all results collected
	 */
	protected static final String BATCH_COMPLETE = "batchcomplete";

	/**
	 * Root Level
	 * Marks Batch Not Complete
	 * Contains Continue Values
	 */
	protected static final String CONTINUE = "continue";

	/**
	 * Root Level
	 * Entry for Data
	 */
	protected static final String QUERY = "query";

	/**
	 * Query Level
	 * Entry for Pages in Query
	 */
	protected static final String PAGES = "pages";

	/**
	 * Page Level
	 * Marks Wikipedia PageId in Page
	 *
	 * Links Here key Value Object Level
	 * Key Value for int of pageid
	 */
	protected static final String PAGE_ID = "pageid";

	/**
	 * Continue Level
	 * Key for Links Here Continue Value
	 */
	protected static final String LINKS_HERE_CONTINUE = "lhcontinue";

	/**
	 * Page Level
	 * Contains Array of Objects that contain PageId:####
	 */
	protected static final String LINKS_HERE = "linkshere";

	/**
	 * Page Level
	 * Key Value for Title of Page
	 */
	protected static final String PAGE_TITLE = "title";

}
