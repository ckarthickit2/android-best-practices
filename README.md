# Meal DB

## Build Status

[![Master CI](https://img.shields.io/circleci/build/github/ckarthickit2/android-best-practices/master)](https://circleci.com/gh/ckarthickit2/android-best-practices/tree/master)

## Landing Page 

![MealDB Landing Page][meal_db_catalog]

## Tools Used

1. [Editor Config][editor_config] - Helps maintain __consistent codestyle__ for multiple developers
working on same project __across various editors and IDEs__
- [EditorConfig for XCode][editor_config_xcode].
- [EditorConfig for VS Code][editor_config_vscode].
- `Intellij IDEA` / `Android Studio` has default support.

2. [Klint][klint] - An anti-[bikeshedding] Kotlin `lint`-er with built-in `formatter` which respects `.editorconfig`
- Gradle Plugins for Klint Tool
	- https://github.com/jlleitschuh/ktlint-gradle - automatically creates check and format tasks for project Kotlin sources
	- https://github.com/jeremymailen/kotlinter-gradle
	- https://github.com/diffplug/spotless - allows you to keep license headers, markdown documentation, etc. in check.
3. [Detekt][detekt] - Static Code analysis for Kotlin.
- Gradle Plugins for Static Analysis
	- https://github.com/novoda/gradle-static-analysis-plugin
4. [Shields IO][shields_io] - For README badges
5. [Danger][danger_integration_guide]

## Setting Up Tools

### Unit Testing

- [Mockk Library][mockk]
- [Mocks aren't Stubs][mock_vs_stubs]

### Git-Hooks

-  Currently Targeting only `MacOS` and `Linux` machines.
-  [Git Hooks][git_hooks_gradle] script takes care of copying the git-hooks before clean/assemble.
- Relies on the fact that developers will at-least `clean` or `assemble` once before committing any code.

---

## BikeShedding
AKA, Parkinson's Law of triviality
- argument that members of an organization give disproportionate weight to trivial issues.
- Parkinson provides the example of a fictional committee whose job was to approve the plans for a nuclear power plant spending the majority of its time on discussions about relatively minor but easy-to-grasp issues, such as what materials to use for the staff bike shed, while neglecting the proposed design of the plant itself, which is far more important and a far more difficult and complex task.
Occurs when a development team spends a disproportionate amount of time and effort on a trivial or unimportant detail of a system, such as the color of a bikeshed

---

[editor_config]: https://editorconfig.org/
[klint]: https://github.com/pinterest/ktlint
[detekt]: https://github.com/arturbosch/detekt
[editor_config_vscode]: https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig
[editor_config_xcode]: https://github.com/MarcoSero/EditorConfig-Xcode
[shields_io]: https://shields.io/
[git_hooks_gradle]: team-props/git-hooks.gradle
[meal_db_catalog]: art/mealdb-catalog.png
[mockk]: https://github.com/mockk/mockk
[mock_vs_stubs]: https://martinfowler.com/articles/mocksArentStubs.html#TheDifferenceBetweenMocksAndStubs
[danger_integration_guide]: https://gist.github.com/ckarthickit/53156ed9080affe5cae31a5645d4fc11
