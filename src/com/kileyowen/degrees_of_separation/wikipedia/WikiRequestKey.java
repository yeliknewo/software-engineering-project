
package com.kileyowen.degrees_of_separation.wikipedia;

public enum WikiRequestKey {
	PAGE_TITLES {

		@Override
		public String toString() {

			return "titles";

		}
	},

	PROP {

		@Override
		public String toString() {

			return "prop";

		}
	},
	PAGE_IDS {

		@Override
		public String toString() {

			return "pageids";

		}
	},
	LINKS_HERE_PROP {

		@Override
		public String toString() {

			return "lhprop";
		}

	},
	LINKS_HERE_NAMESPACE {

		@Override
		public String toString() {

			return "lhnamespace";

		}

	},
	LINKS_HERE_CONTINUE {

		@Override
		public String toString() {

			return "lhcontinue";

		}

	},
	LINKS_HERE_LIMIT {

		@Override
		public String toString() {

			return "lhlimit";

		}

	},

	REDIRECT {

		@Override
		public String toString() {

			return "redirects";

		}
	};

}
