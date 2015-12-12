# JetPack TODOs

### New Project Setup

* create a leiningen template for a new website that sets up the necessary files
and directory structure.  (Could be something done by the eventual jar file
itself, but for now a lein template is quick and easy.)
- standard clojure project structure with src/ns/...
  * layouts ns with example code based layouts
- layouts dir with examples using hiccup, html, markdown
- pages dir with example pages using hiccup, html, markdown
- create a users.edn file defining usernames and passwords (encrypted?)

http://yogthos.net/blog/34-Creating+Leiningen+Templates


* jekyll grabs the first chunk of text (defined by an excerpt_separator) and
attaches it as the post.exceprt to be used for blog post listings with read_more
links.

* Add a pagination concept ala jekyll if it seems needed
