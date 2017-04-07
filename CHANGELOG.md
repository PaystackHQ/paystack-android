# Change Log
Last updated Sep 5, 2016
(from http://keepachangelog.com/)

All notable changes to this project will be documented in this file.

## Released v3.0.0 on Apr 7, 2017
- Add access_code support (`chargeCard` will resume transaction initialized on server)
- Transaction reference can be fetched even if an error occured
- removed support for token flow
- Added support for Bank hosted authorization

## Released v2.1.0 on Nov 13, 2016
- Add metadata support.
- HOTFIX Verve card support fully functional

## Released v2.0.2 on Sep 5, 2016
- Add split payment awareness.

## Released v2.0.1 on Sep 5, 2016
- Increase all connection timeout to 5 minutes.

## Released v2.0 - 2016-08-25
- Added Perform Transaction
- Added Verve Card Support
- Added PIN and OTP

## Released v1.2.1 - 2016-07-30
- Update retrofit and okHttp
- okHttp no more uses reflection so we get it the default trust manager

## Released v1.2.0 - 2016-03-14
- ApiClient will throw new exceptions in case `TLSv1.2` is not found
- Make `retrofit` use custom `okHttpClient` that uses our socket factory to create requests

**Fixed**
- Rework Android Library to work with only TLS v1.2

## NEXT - YYYY-MM-DD

## Added - 2016-03-02
- Markdown templates - @ibrahimlawal

## Released v1.1.1 - 2016-03-01
- Updates Gradle plugin to 1.5.0 > @ibrahimlawal
- Updated Retrofit to Retrofit 2 > @ibrahimlawal

**Removed**
- Retrofit

**Added**
- Retrofit 2

**Fixed**
- Compatibility with apps using Retrofit 2

## Released v1.1.0 - 2016-01-26
- Updated readme to include instructions for charging tokens, charging returning customers - @shollsman  
- RSA KEY now a constant fixed in library. - @ibrahimlawal

## Released v1.1.0 - 2015-10-07
- Initial release published - @segunfamisa

## Commits on Oct 6, 2015
- Added files > @segunfamisa - 3472b97
- Initial commit > @shollsman


### Security
- Nothing
