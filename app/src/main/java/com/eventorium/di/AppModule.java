package com.eventorium.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.eventorium.BuildConfig;
import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.auth.repositories.RoleRepository;
import com.eventorium.data.auth.repositories.UserReportRepository;
import com.eventorium.data.auth.repositories.UserRepository;
import com.eventorium.data.auth.services.AuthService;
import com.eventorium.data.auth.services.RoleService;
import com.eventorium.data.auth.services.UserReportService;
import com.eventorium.data.auth.services.UserService;
import com.eventorium.data.category.repositories.CategoryProposalRepository;
import com.eventorium.data.category.repositories.CategoryRepository;
import com.eventorium.data.category.services.CategoryProposalService;
import com.eventorium.data.category.services.CategoryService;
import com.eventorium.data.company.repositories.CompanyRepository;
import com.eventorium.data.company.services.CompanyService;
import com.eventorium.data.event.repositories.AccountEventRepository;
import com.eventorium.data.event.repositories.BudgetRepository;
import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.event.repositories.EventTypeRepository;
import com.eventorium.data.event.repositories.InvitationRepository;
import com.eventorium.data.event.services.AccountEventService;
import com.eventorium.data.event.services.BudgetService;
import com.eventorium.data.event.services.EventService;
import com.eventorium.data.event.services.EventTypeService;
import com.eventorium.data.event.services.InvitationService;
import com.eventorium.data.interaction.repositories.ChatRepository;
import com.eventorium.data.interaction.repositories.ChatRoomRepository;
import com.eventorium.data.interaction.repositories.CommentRepository;
import com.eventorium.data.interaction.repositories.RatingRepository;
import com.eventorium.data.interaction.services.ChatRoomService;
import com.eventorium.data.interaction.services.ChatService;
import com.eventorium.data.interaction.services.CommentService;
import com.eventorium.data.interaction.services.RatingService;
import com.eventorium.data.notification.repositories.NotificationRepository;
import com.eventorium.data.notification.services.NotificationService;
import com.eventorium.data.shared.repositories.CityRepository;
import com.eventorium.data.shared.services.CityService;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.repositories.PriceListRepository;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.solution.repositories.ReservationRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.solution.services.AccountProductService;
import com.eventorium.data.solution.services.AccountServiceService;
import com.eventorium.data.solution.services.PriceListService;
import com.eventorium.data.solution.services.ProductService;
import com.eventorium.data.solution.services.ReservationService;
import com.eventorium.data.solution.services.ServiceService;
import com.eventorium.data.util.AuthInterceptor;
import com.eventorium.data.util.adapters.LocalDateTimeAdapter;
import com.eventorium.data.util.services.WebSocketService;
import com.eventorium.data.util.adapters.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
@NoArgsConstructor
public class AppModule {

    private static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/v1/";

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return new Retrofit.Builder()
                .baseUrl(SERVICE_API_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public WebSocketService provideWebSocketService() {
        return new WebSocketService();
    }
    @Provides
    @Singleton
    public static OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();
    }


    @Provides
    @Singleton
    public Context provideContext(@ApplicationContext Context context) {
        return context;
    }

    @Provides
    @Singleton
    public static SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public static AuthInterceptor provideAuthInterceptor(SharedPreferences sharedPreferences, @ApplicationContext Context context) {
        return new AuthInterceptor(sharedPreferences, context);
    }

    @Provides
    @Singleton
    @Inject
    public CategoryService provideCategoryService(Retrofit retrofit) {
        return retrofit.create(CategoryService.class);
    }

    @Provides
    @Singleton
    public static CategoryRepository provideCategoryRepository(CategoryService categoryService) {
        return new CategoryRepository(categoryService);
    }

    @Provides
    @Singleton
    @Inject
    public EventTypeService provideEventTypeService(Retrofit retrofit) {
        return retrofit.create(EventTypeService.class);
    }
    @Provides
    @Singleton
    public static EventTypeRepository provideEventTypeRepository(EventTypeService service) {
        return new EventTypeRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public ServiceService provideServiceService(Retrofit retrofit) {
        return retrofit.create(ServiceService.class);
    }

    @Provides
    @Singleton
    public static ServiceRepository provideServiceRepository(ServiceService serviceService) {
        return new ServiceRepository(serviceService);
    }

    @Provides
    @Singleton
    @Inject
    public ProductService provideProductService(Retrofit retrofit) {
        return retrofit.create(ProductService.class);
    }

    @Provides
    @Singleton
    public ProductRepository provideProductRepository(ProductService productService) {
        return new ProductRepository(productService);
    }

    @Provides
    @Singleton
    @Inject
    public AccountServiceService provideAccountServiceService(Retrofit retrofit) {
        return retrofit.create(AccountServiceService.class);
    }

    @Provides
    @Singleton
    @Inject
    public CategoryProposalService provideCategoryProposalService(Retrofit retrofit) {
        return retrofit.create(CategoryProposalService.class);
    }

    @Provides
    @Singleton
    public CategoryProposalRepository provideCategoryProposalRepository(
            CategoryProposalService service
    ) {
        return new CategoryProposalRepository(service);
    }

    @Provides
    @Singleton
    public static AccountServiceRepository provideAccountServiceRepository(AccountServiceService service) {
        return new AccountServiceRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public AuthService provideAuthService(Retrofit retrofit) {
        return retrofit.create(AuthService.class);
    }

    @Provides
    @Singleton
    public static AuthRepository authRepository(
            WebSocketService webSocketService,
            AuthService service,
            SharedPreferences sharedPreferences
    ) {
        return new AuthRepository(webSocketService, service, sharedPreferences);
    }


    @Provides
    @Singleton
    public static AccountProductRepository provideAccountProductRepository(AccountProductService service) {
        return new AccountProductRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public AccountProductService provideAccountProductService(Retrofit retrofit) {
        return retrofit.create(AccountProductService.class);
    }

    @Provides
    @Singleton
    public PriceListService providePriceListService(Retrofit retrofit) {
        return retrofit.create(PriceListService.class);
    }

    @Provides
    @Singleton
    public PriceListRepository providePriceListRepository(PriceListService priceListService) {
        return new PriceListRepository(priceListService);
    }


    @Provides
    @Singleton
    public static EventRepository provideEventRepository(EventService service){
        return new EventRepository(service);
    }

    @Provides
    @Singleton
    public EventService provideEventService(Retrofit retrofit){
        return retrofit.create(EventService.class);
    }

    @Provides
    @Singleton
    public static BudgetRepository provideBudgetRepository(BudgetService service){
        return new BudgetRepository(service);
    }

    @Provides
    @Singleton
    public BudgetService provideBudgetService(Retrofit retrofit){
        return retrofit.create(BudgetService.class);
    }

    @Provides
    @Singleton
    public static CityRepository provideCityRepository(CityService service){
        return new CityRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public CityService provideCityService(Retrofit retrofit){
        return retrofit.create(CityService.class);
    }

    @Provides
    @Singleton
    public static ChatRepository provideChatRepository(ChatService chatService){
        return new ChatRepository(chatService);
    }

    @Provides
    @Singleton
    public ChatService provideChatService(Retrofit retrofit){
        return retrofit.create(ChatService.class);
    }

    @Provides
    @Singleton
    public static RoleRepository provideRoleRepository(RoleService service){
        return new RoleRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public RoleService provideRoleService(Retrofit retrofit){
        return retrofit.create(RoleService.class);
    }

    @Provides
    @Singleton
    public static UserRepository provideUserRepository(UserService service){
        return new UserRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public UserService provideUserService(Retrofit retrofit){
        return retrofit.create(UserService.class);
    }

    @Provides
    @Singleton
    public static InvitationRepository provideInvitationRepository(InvitationService service) {
        return new InvitationRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public InvitationService provideInvitationService(Retrofit retrofit) {
        return retrofit.create(InvitationService.class);
    }

    @Provides
    @Singleton
    public static CompanyRepository provideCompanyRepository(CompanyService service) {
        return new CompanyRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public CompanyService provideCompanyService(Retrofit retrofit) {
        return retrofit.create(CompanyService.class);
    }

    @Provides
    @Singleton
    public static UserReportRepository provideUserReportRepository(UserReportService service) {
        return new UserReportRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public UserReportService provideUserReportService(Retrofit retrofit) {
        return retrofit.create(UserReportService.class);
    }

    @Provides
    @Singleton
    public static AccountEventRepository provideAccountEventRepository(AccountEventService service) {
        return new AccountEventRepository(service);
    }

    @Provides
    @Singleton
    @Inject
    public AccountEventService provideAccountEventService(Retrofit retrofit) {
        return retrofit.create(AccountEventService.class);
    }

    @Provides
    @Singleton
    public CommentService provideCommentService(Retrofit retrofit) {
        return retrofit.create(CommentService.class);
    }

    @Provides
    @Singleton
    public CommentRepository provideCommentRepository(CommentService commentService) {
        return new CommentRepository(commentService);
    }

    @Provides
    @Singleton
    public RatingService provideRatingService(Retrofit retrofit) {
        return retrofit.create(RatingService.class);
    }

    @Provides
    @Singleton
    public RatingRepository provideReviewRepository(RatingService ratingService) {
        return new RatingRepository(ratingService);
    }


    @Provides
    @Singleton
    public static NotificationRepository provideNotificationRepository(NotificationService service) {
        return new NotificationRepository(service);
    }

    @Provides
    @Singleton
    public NotificationService provideNotificationService(Retrofit retrofit) {
        return retrofit.create(NotificationService.class);
    }

    @Provides
    @Singleton
    public ReservationRepository provideReservationRepository(ReservationService service) {
        return new ReservationRepository(service);
    }

    @Provides
    @Singleton
    public ReservationService provideReservationService(Retrofit retrofit) {
        return retrofit.create(ReservationService.class);
    }


    @Provides
    @Singleton
    public static ChatRoomRepository providerChatRoomRepository(ChatRoomService service) {
        return new ChatRoomRepository(service);
    }

    @Provides
    @Singleton
    public ChatRoomService providerChatRoomService(Retrofit retrofit) {
        return retrofit.create(ChatRoomService.class);
    }
}
