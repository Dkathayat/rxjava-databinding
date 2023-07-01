package com.yewapp.ui.modules.listner

import android.os.Bundle
import com.yewapp.data.network.api.challenges.ChallengeModel

class ChallengeExtras {

    companion object {
        //        const val CHALLENGE_DATA_LOCATION = "challenge_data_location"
        const val CHALLENGE_DATA = "challenge_data"
        const val SPORTS_TYPE_ID = "sports_type_id"
        const val SPORTS_TYPE = "sports_type"
        const val RIDER_TYPE = "rider_type"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val ROUTE_ID = "route_Id"
        const val NAVIGATE_TO_ROUTE_LIST = "navigate_to_route_list"

        inline fun challengeExtras(block: ChallengeExtras.Builder.() -> Unit) =
            ChallengeExtras.Builder()
                .apply(block)
                .build()
    }

    class Builder {
        //        lateinit var challengeModelLocationData: ChallengeModelLocationData
        lateinit var challengeData: ChallengeModel
        var riderType = ""
        var sportsType = ""
        var sportsTypeID = ""
        var startDate = ""
        var endDate = ""
        var routeId = ""
        var navigateTO = 0
        fun build(): Bundle {
            return Bundle().apply {
                putParcelable(ChallengeExtras.CHALLENGE_DATA, challengeData)
                putString(ChallengeExtras.RIDER_TYPE, riderType)
                putString(ChallengeExtras.SPORTS_TYPE, sportsType)
                putString(ChallengeExtras.SPORTS_TYPE_ID, sportsTypeID)
                putString(ChallengeExtras.START_DATE, startDate)
                putString(ChallengeExtras.END_DATE, endDate)
                putString(ChallengeExtras.ROUTE_ID, routeId)
                putInt(ChallengeExtras.NAVIGATE_TO_ROUTE_LIST, navigateTO)


            }
        }
    }


    /*
         setting and getting challengeTypeId field
    */
    private var challengeTypeId: Int = 0

    fun setChallengeId(challengeTypeId: Int) {
        this.challengeTypeId = challengeTypeId
    }

    fun getChallengeId(): Int {
        return this.challengeTypeId
    }

    /*
         setting and getting challengeName field
    */
    private var challengeName: String = ""

    fun setChallengeName(challengeName: String) {
        this.challengeName = challengeName
    }

    fun getChallengeName(): String {
        return this.challengeName
    }

    /*
     setting and getting description field
    */
    private var description: String = ""

    fun setDescription(description: String) {
        this.description = description
    }

    fun getDescription(): String {
        return this.description
    }

    /*
     setting and getting description field
     */
    private var dateRange: String = ""
    private var startDate: String = ""
    private var endDate: String = ""


    fun setStartDate(startDate: String) {
        this.startDate = startDate
    }

    fun getStartDate(): String {
        return this.startDate
    }

    fun setEndDate(startDate: String) {
        this.endDate = startDate
    }

    fun getEndDate(): String {
        return this.endDate
    }


    fun setDateRange(dateRange: String) {
        this.dateRange = dateRange
    }

    fun getDateRange(): String {
        return this.dateRange
    }

    /*
  setting and getting challenge visibility field
  */
    private var challengeVisibility: String = ""

    fun setChallengeVisibility(challengeVisibility: String) {
        this.challengeVisibility = challengeVisibility
    }

    fun getChallengeVisibility(): String {
        return this.challengeVisibility
    }

    /*
   setting and getting sports Level field
   */
    private var sportsLevel: String = ""

    fun setSportsLevel(sportsLevel: String) {
        this.sportsLevel = sportsLevel
    }

    fun getSportsLevel(): String {
        return this.sportsLevel
    }

    /*
  setting and getting age Group field
  */
    private var ageGroup: String = ""

    fun setAgeGroup(ageGroup: String) {
        this.ageGroup = ageGroup
    }

    fun getAgeGroup(): String {
        return this.ageGroup
    }

    /*
 setting and getting challenge area field
 */
    private var challengeArea: String = ""

    fun setChallengeArea(challengeArea: String) {
        this.challengeArea = challengeArea
    }

    fun getChallengeArea(): String {
        return this.challengeArea
    }

    /*
setting and getting country field
*/
    private var countryId: String = ""

    fun setCountryId(countryId: String) {
        this.countryId = countryId
    }

    fun getCountryId(): String {
        return this.countryId
    }

    /*
setting and getting stateId field
*/
    private var stateId: String = ""

    fun setStateId(stateId: String) {
        this.stateId = stateId
    }

    fun getStateId(): String {
        return this.stateId
    }

    /*
setting and getting cityId field
*/
    private var cityId: Array<String> = arrayOf()

    fun setCityId(cityId: Array<String>) {
        this.cityId = cityId
    }

    fun getCityId(): Array<String> {
        return this.cityId
    }

    /*
setting and getting zipcode field
*/
    private var zipCode: Array<String> = arrayOf()

    fun setZipcode(zipCode: Array<String>) {
        this.zipCode = zipCode
    }

    fun getZipCode(): Array<String> {
        return this.zipCode
    }

    /*
setting and getting location field
*/
    private var location: String = ""

    fun setLocation(location: String) {
        this.location = location
    }

    fun getLocation(): String {
        return this.location
    }

    /*
setting and getting locationLatitude field
*/
    private var locationLatitude: String = ""

    fun setLocationLatitude(locationLatitude: String) {
        this.locationLatitude = locationLatitude
    }

    fun getLocationLatitude(): String {
        return this.locationLatitude
    }

    /*
setting and getting locationLongitude field
*/
    private var locationLongitude: String = ""

    fun setLocationLongitude(locationLongitude: String) {
        this.locationLongitude = locationLongitude
    }

    fun getLocationLongitude(): String {
        return this.locationLongitude
    }

    /*
setting and getting radius field
*/
    private var radius: Int? = null

    fun setRadius(radius: Int) {
        this.radius = radius
    }

    fun getRadius(): Int {
        return this.radius!!
    }

    /*
setting and getting calories field
*/
    private var calories: String = ""

    fun setCalories(calories: String) {
        this.calories = calories
    }

    fun getCalories(): String {
        return this.calories
    }

    /*
setting and getting miles field
*/
    private var miles: String = ""

    fun setMiles(miles: String) {
        this.miles = miles
    }

    fun getMiles(): String {
        return this.miles
    }

    /*
setting and getting elevationGain field
*/
    private var elevationGain: String = ""

    fun setElevationGain(miles: String) {
        this.miles = miles
    }

    fun getElevationGain(): String {
        return this.miles
    }

    /*
setting and getting avgWatt field
*/
    private var avgWatt: String = ""

    fun setAvgWatt(avgWatt: String) {
        this.avgWatt = avgWatt
    }

    fun getAvgWatt(): String {
        return this.avgWatt
    }

    /*
setting and getting time field
*/
    private var time: String = ""

    fun setTime(time: String) {
        this.time = time
    }

    fun getTime(): String {
        return this.time
    }

    /*
setting and getting max members field
*/
    private var maxMember: String = ""

    fun setMaxMember(maxMember: String) {
        this.maxMember = maxMember
    }

    fun getMaxMember(): String {
        return this.maxMember
    }

    /*
 setting and getting winnerPicked field
 */
    private var winnerPicked: String = ""

    fun setWinnerPicked(winnerPicked: String) {
        this.winnerPicked = winnerPicked
    }

    fun getWinnerPicked(): String {
        return this.winnerPicked
    }

    /*
setting and getting winnerPrize field
*/
    private var winnerPrizeAwarded: String = ""

    fun setWinnerPrizeAwarded(winnerPrizeAwarded: String) {
        this.winnerPrizeAwarded = winnerPrizeAwarded
    }

    fun getWinnerPrizeAwarded(): String {
        return this.winnerPrizeAwarded
    }

    /*
setting and getting overview field
*/
    private var overview: String = ""

    fun setOverView(overview: String) {
        this.overview = overview
    }

    fun getOverView(): String {
        return this.overview
    }

    /*
setting and getting winner field
*/
    private var winner: String = ""

    fun setWinner(winner: String) {
        this.winner = winner
    }

    fun getWinner(): String {
        return this.winner
    }


    /*
setting and getting additional info field
*/
    private var additionalInfo: String = ""

    fun setAdditionalInfo(additionalInfo: String) {
        this.additionalInfo = additionalInfo
    }

    fun getAdditionalInfo(): String {
        return this.additionalInfo
    }

    /*
setting and getting rules field
*/
    private var rules: String = ""

    fun setRules(rules: String) {
        this.rules = rules
    }

    fun getRules(): String {
        return this.rules
    }

    /*
setting and getting guidlines field
*/
    private var guidelines: String = ""

    fun setGuidelines(guidelines: String) {
        this.guidelines = guidelines
    }

    fun getGuidelines(): String {
        return this.guidelines
    }

    /*
setting and getting guidlines field
*/
    private var qualification: String = ""

    fun setQualification(qualification: String) {
        this.qualification = qualification
    }

    fun getQualification(): String {
        return this.qualification
    }

    /*
setting and getting challengeIcon field
*/
    private var challengeIcon: String = ""

    fun setChallengeIcon(challengeIcon: String) {
        this.challengeIcon = challengeIcon
    }

    fun getChallengeIcon(): String {
        return this.challengeIcon
    }

    /*
setting and getting bannerIcon field
*/
    private var bannerIcon: String = ""

    fun setBannerIcon(bannerIcon: String) {
        this.bannerIcon = bannerIcon
    }

    fun getBannerIcon(): String {
        return this.bannerIcon
    }
}