# Sometimes it's a README fix, or something like that - which isn't relevant for
# including in a project's CHANGELOG for example
declared_trivial = github.pr_title.include? "#trivial"

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("The PR is too big. Try splitting it up") if git.lines_of_code > 20

# Warn when there is no summary
warn("Please provide a summary in the Pull Request description") if github.pr_body.length < 5


# Don't let testing shortcuts get into master by accident
fail("fdescribe left in tests") if `grep -r fdescribe specs/ `.length > 1
fail("fit left in tests") if `grep -r fit specs/ `.length > 1


# ktlint
checkstyle_format.base_path = Dir.pwd
checkstyle_format.report 'app/build/reports/detekt/detekt.xml'


# APK Analyzer

apkstats.command_type=:apk_analyzer # required
#apkstats.apkanalyzer_path='app/build/outputs/apk/debug/app-debug.apk'
apkstats.apk_filepath='app/build/outputs/apk/debug/app-debug.apk' # required
apkstats.file_size
apkstats.permissions
#apkstats.reference_count
#apkstats.dex_count


# APK Size
apk_size = (File.size('app/build/outputs/apk/debug/app-debug.apk').to_f / 2**20).round(2)
message "Debug APK Size: #{apk_size} MB"
