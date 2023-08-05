package com.begletsov.redbook

import com.begletsov.redbook.models.Category
import com.begletsov.redbook.models.Description
import com.begletsov.redbook.models.Geopoint
import com.begletsov.redbook.models.Place
import java.util.UUID

object Data {
    lateinit var categories: List<Category>
    init {
        categories = listOf(
            Category(
                name = "Музеи",
                iconFilePath = "https://cdn-icons-png.flaticon.com/512/1183/1183878.png",
            ),
            Category(
                name = "Театры",
                iconFilePath = "https://cdn-icons-png.flaticon.com/512/1838/1838392.png",
            ),
            Category(
                name = "Парки и сады",
                iconFilePath = "https://cdn-icons-png.flaticon.com/512/195/195649.png",
            ),
            Category(
                name = "Соборы",
                iconFilePath = "https://cdn-icons-png.flaticon.com/512/3056/3056106.png",
            ),
            Category(
                name = "Дворцы",
                iconFilePath = "https://cdn-icons-png.flaticon.com/512/5626/5626899.png",
            ),
            Category(
                name = "Достопримечательности",
                iconFilePath = "https://cdn-icons-png.flaticon.com/512/1207/1207259.png",
            )
        )

        for (category in categories) {
            when (category.name) {
                "Музеи" -> {
                    category.places.addAll(listOf(
                        Place(
                            description = Description(
                                name = "Государственный Эрмитаж",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://www.advantour.com/russia/images/city/st-peterburg/landmarks-and-attractions/saint-petersburg-landmarks-and-attractions-hermitage.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Русский музей",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://spb.101novostroyka.ru/upload/iblock/6a3/6a38ec837393b5345139b30d80decd3f.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Музей Эрарта",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://upload.wikimedia.org/wikipedia/commons/a/a1/Erarta_Museum_and_Galleries_of_Contemporary_Art.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Кунсткамера",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://www.spbmuzei.ru/wp-content/uploads/2020/09/kuntskamera.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Этнографический музей",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://ar.culture.ru/attachments/attachment/original/6321d3c7a0d42af6dd48a97d-original.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Военно-исторический музей артиллерии",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://upload.wikimedia.org/wikipedia/commons/8/88/%D0%92%D1%85%D0%BE%D0%B4_%D0%B2_%D0%BC%D1%83%D0%B7%D0%B5%D0%B9_%D0%B0%D1%80%D1%82%D0%B8%D0%BB%D0%BB%D0%B5%D1%80%D0%B8%D0%B8.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                    )) }
                "Театры" -> {
                    category.places.addAll(listOf(
                        Place(
                            description = Description(
                                name = "Мариинский театр",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://kuda-spb.ru/uploads/ba778e09d3cf006c40f1365179255bcf.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Александринский театр",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://domir.ru/l-art/images/rossi/rossi-16.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "БУФФ",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://teatrafisha.ru/pics/_1900_1000_90_469728168887962123.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Театр юных зрителей имени А. А. Брянцева",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/03/ef/b7/e2/caption.jpg?w=1200&h=1200&s=1",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Театр Комедии имени Н.П. Акимова",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://prokontora.ru/assets/images/afisha/akimov-min.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                        Place(
                            description = Description(
                                name = "Михайловский театр",
                                title = "",
                                descriptionText = "",
                                imagePath = "https://sforin.ru/dostupnyi-peterburg/userfiles/adminimg/bobjimg/21_0.jpg",
                                audioFilePath = "http://inpeterhof.ru/wp-content/uploads/2018/03/11_.mp3?_=11"
                            ),
                            categoryId = category.id,
                            location = Geopoint(0.0, 0.0)
                        ),
                    )) }
            }
        }
    }
}