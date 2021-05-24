package com.example.diploma;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAPI {

    @POST("api/registration")
    Observable<String> registerUser(@Body User user);

    @POST("api/login")
    Observable<String> loginUser(@Body User user);

    @POST("api/getdoctorsforticket")
    Observable<String> getDoctorsForTicket(@Body Doctor doctor);

    @POST("api/gettimesforticket")
    Observable<String> getTimesForTicket(@Body Tickets ticket);

    @GET("api/getdoctors")
    Call<String> getDoctors();

    @GET("api/getpatient")
    Call<String> getPatients();

    @GET("api/receipt/{id}")
    Call<String> getReceipts(@Path("id") int id);

    @GET("api/ticket/{id}")
    Call<String> getTickets(@Path("id") int id);

    @GET("api/gettickets/{id}")
    Call<String> getTicketsForUser(@Path("id") int id);

    @GET("api/getticketsfordoc/{id}")
    Call<String> getTicketsForDoctor(@Path("id") int id);

    @GET("api/getticketsforhistory/{id}")
    Call<String> getTicketsForHistory(@Path("id") int id);

    //нужно что-то придумать с датами, чтобы когда хочешь записаться подтягивались только даты будущие,
    //то естб прошлые отображать не нужно,
    //в случае с историей посещений сравниваем даты с сегодняшней и прошлые помещаем в отдельные список

    //может еще стоит диалог билдеры поубирать где это не надо AlertDialog

    //сделать возможность отмены в хистори дитэйлс фрагмет
    //просто обновлять айди пациента на 1

    //при регистрации на прием проверять врач на пустоту дабы избежать дампа

    //при регистрациии на прием подтягивать врачей не только при клике отделения но и здания

    //мб придумать чтобы даты на трех экранах (один у юзеров первый по счету
    //остальные у доктора (1 и 3-1 экраны) отображались лучше
    //сейчас: сегодняшняя дата идет в прошлое

    //у админа при редактировании нижняя меню плывет с клавиатурой

    //у админа при редактировании врача сделать спиннеры в двух полях
    @GET("api/getpatient/{id}")
    Call<String> getPatient(@Path("id") int id);

    @GET("api/journal/{id}")
    Call<String> getJournal(@Path("id") int id);

    @PUT("api/updatedoctor")
    Call<Void> updateDoctor(@Body User user);

    @PUT("api/updateuser")
    Call<Void> updatePatient(@Body User user);

    @PUT("api/journal")
    Call<Void> updateJournal(@Body JournalNotes journal);

    @PUT("api/ticket")
    Call<Void> updateTicket(@Body Tickets ticket);

    @DELETE("api/updatedoctor/{id}")
    Call<Void> deleteDoctor(@Path("id") int id);

    @DELETE("api/journal/{id}")
    Call<Void> deleteJournal(@Path("id") int id);

    @POST("api/ticket")
    Observable<String> createSchedule(@Body Tickets ticket);

    @POST("api/receipt")
    Observable<String> createReceipt(@Body Receipt receipt);

    @POST("api/journal")
    Observable<String> createJournal(@Body JournalNotes journal);
//    @GET("api/getevents/{id}")
//    Call<String> getEvents(@Path("id") int id);
//
//    @GET("api/getsignupevents/{id}")
//    Call<String> getSignUpEvents(@Path("id") int id);
//
//    @GET("api/synchronize")
//    Call<String> synchronize();
//
////    @GET("api/getevents")
////    Call<Events> getEventsById(@Query("id") Integer id);
//
////    @GET("users")
////    Call<User> getUserById(@Query("id") Integer id);
//
//    @POST("api/insertevent")
//    Observable<String> insertEvent(@Body Events event);
//
//    @DELETE("api/insertevent/{id}")
//    Call<Void> deleteEvent(@Path("id") int id);
//
//    @POST("api/insertmember")
//    Observable<String> insertMember(@Body Members member);
//
//    @DELETE("api/insertmember/{id}")
//    Call<Void> deleteMembers(@Path("id") int id);
//
//    @DELETE("api/delmember/{id}/{id2}")
//    Observable<String> delMember(@Path("id") int id, @Path("id2") int id2);
}
