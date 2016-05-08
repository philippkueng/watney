# Watney

[![Circle CI](https://circleci.com/gh/philippkueng/watney.svg?style=svg)](https://circleci.com/gh/philippkueng/watney)
[![Join the chat at https://gitter.im/philippkueng/watney](https://badges.gitter.im/philippkueng/watney.svg)](https://gitter.im/philippkueng/watney?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

HTML to Markdown converter

## Tests

```bash
lein midje
```

Or let the tests run automatically on file changes during development.

```bash
lein repl
user=> (use 'midje.repl)
user=> (autotest)
```

## Limitations

Since both HTML and Markdown comes in a variety of ways, there are some constraints that were introduced to make the generated Markdown file as correct as possible.

* Unordered Lists are always converted to `* ` with a single space.
* List indentations are always 2 spaces.
* Ordered Lists are always converted to `1. ` with a single space.
* Between every change of HTML element there will be a newline. `<h1>H1</h1><h3>H3</h3> => # H1\n##H3` 
## License

Copyright © 2016 Philipp Küng

Distributed under the Eclipse Public License, the same as Clojure.