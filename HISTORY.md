# History
Last updated Nov 4, 2017

## Commits on Nov 4, 2017
- Resolve issue that caused build to fail when using AAPT2

## Commits on Oct 27, 2017
- Invalid card details will force us to show a popup that allows the user correct their card entry
- Updated all dependencies
- In debug mode, an assertion has been added to force the developer to choose either remote or local
transaction initialization exclusively.
- Added utility method to strip card of all non-numeric characters

## Commits on Jun 20, 2017
- Fixed a bug that made some verve cards get bounced by the validator
- Other minor bug fixes

## Commits on Apr 7, 2017
- `chargeCard` now resumes a transaction initialized by server
- Remove deprecated methods
- Add support for SecureCode
- Bug fixes

## Commits on Sep 5, 2016
- Increase all connection timeout to 5 minutes.

## Commits on Aug 24, 2016
- Add chargeCard
- Deprecate PaystackSdk.createToken
- Deprecate createToken

## Commits on Aug 24, 2016
- Add chargeCard
- Deprecate PaystackSdk.createToken
- Deprecate createToken

## Commits on Mar 14, 2016
- Bump `PaystackSdk` version to `1.2.0`
- Example App will use `v1.2.0`
- Added default sample card to example activity so token request is one-click
- Added `TLSSocketFactory` class that pegs all socket calls to use `TLSv1.2`
- ApiClient will throw new exceptions in case `TLSv1.2` is not found
- Make `retrofit` use custom `okHttpClient` that uses `TLSSocketFactory` to create requests
- Took out stray `statuscode` integer

## Commits on Mar 2, 2016
- Added Markdown templates > @ibrahimlawal - bb030a6  
- Example app should use published library > ibrahimlawal - 657dc7c

## Commits on Mar 1, 2016
- Fix javadoc errors and warnngs > @ibrahimlawal - b852006  
- Update README.md for release of v1.1.1 > @ibrahimlawal - ba7ab2a  
- Updates Gradle plugin to 1.5.0 > @ibrahimlawal - 415101b
- Updated Retrofit to Retrofit 2 > @ibrahimlawal - c4bfadb  

## Commits on Feb 12, 2016
Added sample code using PHP library/class > @ibrahimlawal - a343243  

## Commits on Jan 28, 2016
Version 1.1.0 published to bintray > @ibrahimlawal - a4c2e55  

## Commits on Jan 26, 2016
- added instructions for charging returning customers > @shollsman - 3324b1b  
- charge token endpoint updated > @shollsman - c08c755  
- RSA KEY now a constant fixed in library. > @ibrahimlawal - 0df1adf  
- Updated readme to include instructions for charging tokens > @shollsman - aa0345a

## Commits on Oct 9, 2015
- Updated README.md > segunfamisa - 6a3bcf4

## Commits on Oct 7, 2015
- Updated README.md > @segunfamisa - 268ea4a  
- Merge branch 'master' > @segunfamisa - e8d3caa  
- Fixed gradle for upload to maven > @segunfamisa - 95f3aeb  

## Commits on Oct 6, 2015
- Added files > @segunfamisa - 3472b97
- Initial commit > @shollsman
