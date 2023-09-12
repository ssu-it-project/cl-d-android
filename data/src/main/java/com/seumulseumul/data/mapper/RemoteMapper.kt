package com.seumulseumul.data.mapper

import com.seumulseumul.data.model.remote.request.Device
import com.seumulseumul.data.model.remote.request.RequestAuthRefreshToken
import com.seumulseumul.data.model.remote.request.RequestSignIn
import com.seumulseumul.data.model.remote.request.RequestSignUp
import com.seumulseumul.data.model.remote.response.Author
import com.seumulseumul.data.model.remote.response.ClimbingGym
import com.seumulseumul.data.model.remote.response.ClimbingGymInfo
import com.seumulseumul.data.model.remote.response.Date
import com.seumulseumul.data.model.remote.response.Location
import com.seumulseumul.data.model.remote.response.Pagination
import com.seumulseumul.data.model.remote.response.Place
import com.seumulseumul.data.model.remote.response.Record
import com.seumulseumul.data.model.remote.response.ResponseAuthToken
import com.seumulseumul.data.model.remote.response.ResponseClimeGyms
import com.seumulseumul.data.model.remote.response.ResponseClimeRecords
import com.seumulseumul.data.model.remote.response.Term
import com.seumulseumul.domain.model.Agreement
import com.seumulseumul.domain.model.AuthToken
import com.seumulseumul.domain.model.ClimeRecords
import com.seumulseumul.domain.model.Gyms
import com.seumulseumul.domain.model.RefreshToken
import com.seumulseumul.domain.model.SignIn
import com.seumulseumul.domain.model.SignUp
import javax.inject.Inject

class RemoteMapper @Inject constructor(
) {

    /* ----------------------------Request---------------------------- */
    fun mapperToRefreshToken(
        requestAuthRefreshToken: RequestAuthRefreshToken
    ): RefreshToken = RefreshToken(
        deviceId = requestAuthRefreshToken.deviceId,
        refreshToken = requestAuthRefreshToken.refreshToken
    )

    fun mapperToRequestAuthRefreshToken(
        refreshToken: RefreshToken
    ): RequestAuthRefreshToken = RequestAuthRefreshToken(
        deviceId = refreshToken.deviceId,
        refreshToken = refreshToken.refreshToken
    )

    fun mapperToSignIn(
        requestSignIn: RequestSignIn
    ): SignIn = SignIn(
        device = mapperToDevice(requestSignIn.device),
        accessToken = requestSignIn.accessToken,
        loginType = requestSignIn.loginType
    )

    fun mapperToRequestSignIn(
        signIn: SignIn
    ): RequestSignIn = RequestSignIn(
        device = mapperToRequestDevice(signIn.device),
        accessToken = signIn.accessToken,
        loginType = signIn.loginType
    )

    fun mapperToRequestSignUp(
        requestSignUp: RequestSignUp
    ): SignUp {
        val agreementList = mutableListOf<Agreement>()
        requestSignUp.agreements.forEach {
            agreementList.add(mapperToAgreement(it))
        }
        return SignUp(
            agreementList,
            mapperToAuth(requestSignUp.auth),
            //mapperToProfile(requestSignUp.profile)
        )
    }

    fun mapperToRequestSignUp(
        SignUp: SignUp
    ): RequestSignUp {
        val agreementList = mutableListOf<com.seumulseumul.data.model.remote.request.Agreement>()
        SignUp.agreements.forEach {
            agreementList.add(mapperToRequestAgreement(it))
        }
        return RequestSignUp(
            agreementList,
            mapperToRequestAuth(SignUp.auth),
            //mapperToRequestProfile(SignUp.profile)
        )
    }

    fun mapperToDevice(
        device: Device
    ): com.seumulseumul.domain.model.Device = com.seumulseumul.domain.model.Device(
        deviceId = device.deviceId,
        deviceInfo = device.deviceInfo
    )

    fun mapperToRequestDevice(
        device: com.seumulseumul.domain.model.Device
    ): Device = Device(
        deviceId = device.deviceId,
        deviceInfo = device.deviceInfo
    )

    fun mapperToAgreement(
        agreement: com.seumulseumul.data.model.remote.request.Agreement
    ): com.seumulseumul.domain.model.Agreement = com.seumulseumul.domain.model.Agreement(
        agreement.agreed,
        agreement.id,
        //agreement.timestamp
    )

    fun mapperToRequestAgreement(
        agreement: com.seumulseumul.domain.model.Agreement
    ): com.seumulseumul.data.model.remote.request.Agreement = com.seumulseumul.data.model.remote.request.Agreement(
        agreement.agreed,
        agreement.id,
        //agreement.timestamp
    )

    fun mapperToAuth(
        auth: com.seumulseumul.data.model.remote.request.Auth
    ): com.seumulseumul.domain.model.Auth = com.seumulseumul.domain.model.Auth(
        auth.accessToken,
        mapperToDevice(auth.device),
        auth.loginType
    )

    fun mapperToRequestAuth(
        auth: com.seumulseumul.domain.model.Auth
    ): com.seumulseumul.data.model.remote.request.Auth = com.seumulseumul.data.model.remote.request.Auth(
        auth.accessToken,
        mapperToRequestDevice(auth.device),
        auth.loginType
    )

    /*fun mapperToProfile(
        profile: com.seumulseumul.data.model.remote.request.Profile
    ): com.seumulseumul.domain.model.Profile = com.seumulseumul.domain.model.Profile(
        profile.birthday,
        profile.gender,
        profile.image,
        profile.name,
        profile.nickname
    )*/

    /*fun mapperToRequestProfile(
        profile: com.seumulseumul.domain.model.Profile
    ): com.seumulseumul.data.model.remote.request.Profile = com.seumulseumul.data.model.remote.request.Profile(
        profile.birthday,
        profile.gender,
        profile.image,
        profile.name,
        profile.nickname
    )*/

    /* ----------------------------Response---------------------------- */

    fun mapperToAuthToken(
        responseAuthToken: ResponseAuthToken
    ): AuthToken = AuthToken(
        responseAuthToken.jwt,
        responseAuthToken.refreshToken
    )

    fun mapperToResponseAuthToken(
        authToken: AuthToken
    ): ResponseAuthToken = ResponseAuthToken(
        authToken.jwt,
        authToken.refreshToken
    )

    fun mapperToGyms(
        responseClimeGyms: ResponseClimeGyms
    ): Gyms {
        val gyms = mutableListOf<com.seumulseumul.domain.model.ClimbingGym>()
        responseClimeGyms.climbingGyms.forEach {
            gyms.add(mapperToClimbingGym(it))
        }
        return Gyms(
            gyms,
            mapperToPagination(responseClimeGyms.pagination)
        )
    }

    fun mapperToClimeRecords(
        responseClimeRecords: ResponseClimeRecords
    ): ClimeRecords {
        val records = mutableListOf<com.seumulseumul.domain.model.Record>()
        responseClimeRecords.records.forEach {
            records.add(mapperToRecord(it))
        }
        return ClimeRecords(
            mapperToPagination(responseClimeRecords.pagination),
            records
        )
    }

    fun mapperToTerm(
        term: Term
    ): com.seumulseumul.domain.model.Term = com.seumulseumul.domain.model.Term(
        term.id,
        term.name,
        term.pageUrl
    )

    fun mapperToClimbingGym(
        climbingGym: ClimbingGym
    ): com.seumulseumul.domain.model.ClimbingGym = com.seumulseumul.domain.model.ClimbingGym(
        climbingGym.id,
        mapperToLocation(climbingGym.location),
        mapperToPlace(climbingGym.place),
        climbingGym.type
    )

    fun mapperToPagination(
        pagination: Pagination
    ): com.seumulseumul.domain.model.Pagination = com.seumulseumul.domain.model.Pagination(
        pagination.limit,
        pagination.skip,
        pagination.total
    )

    fun mapperToResponsePagination(
        pagination: com.seumulseumul.domain.model.Pagination
    ): Pagination = Pagination(
        pagination.limit,
        pagination.skip,
        pagination.total
    )

    fun mapperToDate(
        date: Date
    ): com.seumulseumul.domain.model.Date = com.seumulseumul.domain.model.Date(
        date.created,
        date.modified
    )

    fun mapperToResponseDate(
        date: com.seumulseumul.domain.model.Date
    ): Date = Date(
        date.created,
        date.modified
    )

    fun mapperToLocation(
        location: Location
    ): com.seumulseumul.domain.model.Location = com.seumulseumul.domain.model.Location(
        location.distance,
        location.x,
        location.y
    )

    fun mapperToResponseLocation(
        location: com.seumulseumul.domain.model.Location
    ): Location = Location(
        location.distance,
        location.x,
        location.y
    )

    fun mapperToPlace(
        place: Place
    ): com.seumulseumul.domain.model.Place = com.seumulseumul.domain.model.Place(
        place.addressName,
        place.imageUrl,
        place.name,
        place.parking,
        place.roadAddressName,
        place.shower
    )

    fun mapperToRecord(
        record: Record
    ): com.seumulseumul.domain.model.Record = com.seumulseumul.domain.model.Record(
        mapperToAuthor(record.author),
        mapperToClimbingGymInfo(record.climbingGymInfo),
        record.content,
        mapperToDate(record.date),
        record.id,
        record.image,
        record.level,
        record.likeCount,
        record.sector,
        record.video,
        record.viewCount
    )

    fun mapperToAuthor(
        author: Author
    ): com.seumulseumul.domain.model.Author = com.seumulseumul.domain.model.Author(
        author.id,
        author.nickname,
        author.profileImageUrl
    )

    fun mapperToClimbingGymInfo(
        climbingGymInfo: ClimbingGymInfo
    ): com.seumulseumul.domain.model.ClimbingGymInfo = com.seumulseumul.domain.model.ClimbingGymInfo(
        climbingGymInfo.id,
        climbingGymInfo.name
    )

    /* ---------------------------------------------------------------- */

}
