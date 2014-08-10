# JetPack

A Clojure website starter kit.

Inspired by jekyll to allow for quickly getting up a site with some templated
pages, but everything is rendered dynamically to allow Clojure developers to
augment the system with their own logic, web handling, websockets, etc...

* create layouts in the layouts dir using normal HTML/CSS Create pages
* create pages as text files with EDN headers that refer to layouts
* add arbitrary functions and data to add to the system

JetPack uses the [component](https://github.com/stuartsierra/component) library
to manage component lifecycles and allow for smooth repl driven development.

### Setup

Install the less compiler to make the production css:

    $ npm install less -g


### Usage

To develop your site:
     $ lein repl
     > (go)
     ... make edits
     > (refresh)

To run in the foreground:
     $ lein run

To start the site as a daemon run:
     $ lein daemon start site

### Pages

Standard header map fields for pages:

* layout: specify which layout to render the page
* published: boolean flag whether this page is public or not
* tags: a vector of strings used to group and index pages
* permalink: specify a link name other than the default generated URL
* date: the date to attach as metadata rather than the default (last modified)

All other fields in the header will be made available to templates and site
processor functions.

### Layouts

JetPack adds a set of standard keys to the map passed into all layout functions:

* site: map of site wide info plus the user configuration from config.edn
* page: page header map
* content: the rendered page content

The site map:
* time: current time
* pages: list of all pages
* posts: A reverse chronological list of all Posts
* tags: a map of lists (key = tag, list = pages with that tag)

The page map:
* content: raw string content of the page
* title: string page title
* excerpt: un-rendered page excerpt
* url: url of the post to be used for links
* date: assigned page date (generated or specified in header)
* tags: vector of tags
* next: next page relative to posts listing
* previous: prev page relative to posts listing

All pages go under the pages directory, and they have an EDN header followed by
a line separator "---", followed by the body of the page.


