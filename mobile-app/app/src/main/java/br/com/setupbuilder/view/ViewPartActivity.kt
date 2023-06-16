package br.com.setupbuilder.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.setupbuilder.R
import br.com.setupbuilder.adapters.TipsRecyclerAdapter
import kotlinx.android.synthetic.main.view_part_activity.*

class ViewPartActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TipsRecyclerAdapter.ViewHolder>? = null

    private val names = arrayOf(
        "CPU",
        "Motherboard",
        "RAM",
        "GPU",
        "Storage",
        "Monitor",
        "Keyboard",
        "Mouse",
        "Headset",
        "PSU",
        "Case"
    )

    private val key = arrayOf(
        "CPU",
        "Motherboard",
        "RAM",
        "GPU",
        "Storage",
        "Monitor",
        "Keyboard",
        "Mouse",
        "Headset",
        "PSU",
        "Case"
    )

    private val definitions = arrayOf(
        "Unit pengolah pusat atau CPU (Central Processing Unit)" +
                "juga dikenal sebagai prosesor, adalah bagian dari sistem komputer," +
                "yang melaksanakan instruksi program komputer, untuk mengeksekusi" +
                "dasar aritmatika, logika, dan input dan output data.",
        "Ini adalah bagian dari komputer yang bertanggung jawab untuk menghubungkan dan menghubungkan semua komponen." +
                "Selain memungkinkan lalu lintas informasi, kartu juga memberi makan beberapa" +
                "periferal dengan daya listrik yang mereka terima dari catu daya.",
        "Random Access Memory atau Memori Akses Acak (Random Access Memory, sering" +
                "disingkat RAM) adalah jenis memori yang memungkinkan membaca dan menulis, digunakan sebagai" +
                "Memori utama dalam sistem elektronik digital.",
        "Ini adalah komponen perangkat keras komputer yang bertanggung jawab untuk mengatur dan mengontrol fungsi" +
                "tampilan video di layar. Setiap komputer kontemporer menggunakan antarmuka grafis",
        "Ini adalah perangkat yang mampu menyimpan informasi (data) untuk konsultasi atau penggunaan nanti.",
        "Unit yang digunakan untuk menampilkan teks atau grafik yang dihasilkan komputer secara visual.",
        "Jenis perangkat input yang digunakan oleh pengguna untuk entri manual ke dalam sistem data dan perintah.",
        "Perangkat yang terhubung ke komputer dan berfungsi untuk mengirimkan perintah dengan tombol dan perubahan" +
                " posisi kursor pada monitor komputer.",
        "Ini satu set headphone dengan kontrol volume dan mikrofon",
        "Power supply unit atau PSU (Power Supply Unit) adalah perangkat yang menyuplai daya listrik ke sebuah komputer.",
        "Ini adalah kompartemen yang berisi sebagian besar komponen komputer."

    )

    private val photos = arrayOf(
        R.drawable.processador,
        R.drawable.placa_mae,
        R.drawable.ram,
        R.drawable.placa_de_video,
        R.drawable.storage,
        R.drawable.monitor,
        R.drawable.teclado,
        R.drawable.mouse,
        R.drawable.headseat,
        R.drawable.psu_icon,
        R.drawable.case_icon
    )

    private val tips = arrayOf(
        arrayOf(
            "Tergantung pada jenis penggunaan Anda untuk komputer, lebih disukai lebih banyak atau lebih sedikit inti dan utas (kumpulan tugas yang ada dalam satu atau lebih program, dijalankan pada waktu yang sama oleh prosesor)",
            "Untuk penggunaan di rumah, apakah menjawab dan mengirim email, mengakses Internet dan bermain game yang tidak terlalu menuntut, prosesor 4-core, 4-thread (dikenal sebagai quad-core) sudah cukup",
            "Untuk memainkan game yang lebih menuntut, Anda sudah membutuhkan prosesor quad-core, dengan hyper threading (dua kali lipat jumlah thread konvensional) dan frekuensi tinggi. Contoh: AMD Ryzen 3 3300x dengan 4 core dan 8 thread",
            "Untuk tugas profesional (pengeditan video, pemodelan 3D, dll.) dan bahkan game yang lebih berat, diperlukan prosesor enam inti, juga dengan hyper threading dan pada frekuensi menengah atau tinggi. Contoh: Intel I5 9600K, dengan 6 inti dan 12 inti threads , dan AMD Ryzen 5 3600, juga dengan 6 core dan 12 thread.",
            "Hati-hati jika soket prosesor yang akan Anda beli kompatibel dengan motherboard Anda. Contoh soket: AM4, untuk AMD, dan LG 1151, untuk Intel",
            "Periksa apakah prosesor yang ingin Anda beli memiliki kartu video terintegrasi, jika tidak, Anda perlu membeli kartu video khusus",
            "Beberapa prosesor sudah dilengkapi dengan pendingin di dalam kotak, yang lain tidak, waspadalah."
        ),
        arrayOf(
            "Berikan preferensi pada motherboard dengan setidaknya 2 slot untuk memori RAM;",
            "Periksa apakah chipset (chip yang bertanggung jawab untuk mengontrol berbagai perangkat input dan output) pada motherboard yang ingin Anda beli sudah modern. Contoh: A320, B450, X470, H310, X570, dll;",
            "Hati-hati jika soket (tempat prosesor akan dipasang) di motherboard kompatibel dengan prosesor yang Anda inginkan" +
                    "tempat beli. Setiap generasi prosesor memiliki kompatibilitas dengan jenis soket tertentu. Contoh: AM4, untuk prosesor AMD, LGA 1151, untuk prosesor Intel;",
            "Perhatikan juga faktor bentuk (Form factor) motherboard: ATX, Micro ATX, Mini ITX, dll."
        ),
        arrayOf(
            "Lebih suka memori standar DDR4 (Double-Data-Rate), mereka memiliki peningkatan kinerja 50% dan penghematan daya 40% dibandingkan dengan standar DDR3 sebelumnya;",
            "Berikan preferensi untuk membeli setidaknya sepasang memori RAM dengan ukuran, frekuensi, dan pengaturan waktu yang sama untuk menggunakannya dalam saluran ganda (prosesor berkomunikasi dengan kedua saluran memori secara bersamaan, menggandakan lebar data bus);",
            "Jika Anda tidak berniat menggunakan komputer untuk pekerjaan berat apa pun, dua modul 4GB sudah cukup, dengan total RAM 8GB;",
            "Di sisi lain, jika Anda berniat menggunakan komputer untuk pekerjaan berat seperti mengedit video, pemodelan 3D, atau game berat, idealnya adalah menggunakan dua modul 8GB, dengan total RAM 16GB;",
            "Faktor penting lainnya untuk pengguna yang lebih menuntut adalah frekuensi memori RAM, umumnya bervariasi antara 2400 dan 3200 MHZ, semakin tinggi frekuensinya semakin baik."
        ),
        arrayOf(
            "Tidak perlu memiliki kartu video khusus untuk menggunakan komputer, ada prosesor dengan kartu video terintegrasi, jadi jika Anda tidak akan menggunakan PC untuk bermain game yang sangat menuntut atau pekerjaan profesional, pilih prosesor yang bagus dengan video kartu terintegrasi;\n" +
                    "Di sisi lain, jika Anda ingin melakukan pekerjaan pengeditan video profesional, pemodelan 3D, atau memainkan game yang menuntut, Anda memerlukan kartu grafis khusus;",
            "Pilih model yang lebih baru yang didukung secara luas oleh pabrikan AMD dan NVIDIA.",
            "Contoh AMD: RX 5500XT, RX 5600XT, RX 5700XT, dll;",
            "Contoh NVIDIA: GTX 1650 Super, GTX 1660 Super, RTX 2060, dll;",
            "Berikan preferensi pada kartu video dengan VRAM (memori video) 4 GB atau lebih."
        ),
        arrayOf(
            "Ada dua jenis penyimpanan utama: HDD (Hard Disk Drive) dan" +
                    " SSD (Solid State Drive). NVMe (Non-Volatile Memory express) SSD lebih banyak" +
                    " lebih cepat dari SSD konvensional yang menggunakan bus SATA, yang pada gilirannya adalah" +
                    " lebih cepat dari HDD, oleh karena itu lebih suka SSD NVMe atau SSD SATA untuk menginstal " +
                    "Sistem Operasi dan alat kerja Anda serta HDDS untuk menyimpan file lain;",
            "Ada disk penyimpanan dengan berbagai ukuran, dari SSD 128GB hingga HDD 4TB atau" +
                    " lebih. Konfigurasi level awal yang baik adalah NVMe 128 GB atau SSD SATA, untuk menginstal" +
                    "Sistem Operasi, dan HDD 1TB, untuk menyimpan file lainnya"
        ),
        arrayOf(
            "Memilih monitor yang tepat sangat bergantung pada bagaimana Anda ingin menggunakannya;",
            "Jika Anda adalah pengguna yang tidak main game dan lebih mengutamakan kualitas gambar dan warna, atau bahkan profesional video editing, image correction antara lain pilihlah monitor dengan refresh rate 60Hz dan panel tipe IPS sebaiknya, atau VA ;",
            "Di sisi lain, jika Anda bermain dan memprioritaskan kecepatan respons monitor dan kecepatan penyegaran, pilih monitor dengan respons 1 ms (milidetik), kecepatan penyegaran 120Hz atau lebih dan panel TN;",
            "Namun, jika Anda menginginkan yang terbaik dari kedua dunia dan memiliki uang untuk dibelanjakan, pilih monitor dengan panel IPS, waktu respons rendah, dan kecepatan refresh tinggi."
        ),
        arrayOf(
            "Ada tiga jenis utama keyboard: mekanis, semi-mekanis, dan membran;",
            "Keyboard mekanis dicirikan oleh fungsi masing-masing tombol," +
                    "memungkinkan akurasi yang lebih tinggi, respons sentuhan yang lebih cepat, dan daya tahan yang lebih baik;" +
                    " adalah keyboard yang bagus untuk mereka yang banyak menulis atau bermain game;",
            "Keyboard semi-mekanis memiliki operasi hibrid: tombol yang paling banyak digunakan adalah tombol mekanis " +
                    "dan penggunaan yang lebih sedikit digerakkan oleh membran;" +
                    "Sebaliknya, pada keyboard membran, aktuasi dilakukan melalui membran plastik" +
                    "dari tiga lapisan, yang dimaksud di bawah tombol. Karena drive simultan adalah " +
                    "hampir tidak ada, kebingungan urutan tombol yang ditekan dapat terjadi, sebagai tambahan" +
                    "mereka tidak memiliki banyak perlawanan jika dibandingkan dengan model lainnya.",
            "Jadi, jika Anda banyak mengetik atau bermain dan memiliki uang untuk berinvestasi sedikit lebih banyak, belilah keyboard mekanis."
        ),
        arrayOf(
            "Tergantung pada penggunaan Anda, Anda mungkin memerlukan mouse dengan DPI (Dots Per Inch) yang lebih tinggi, yaitu sensitivitas gerakan yang lebih besar, menyebabkan presisi yang lebih tinggi;",
            "Untuk penggunaan di rumah, mouse dengan DPI tinggi tidak masuk akal, tetapi cocok untuk pengguna yang memainkan game yang menuntut presisi dan sensitivitas gerakan."
        ),
        arrayOf(
            "Perhatikan jika headset yang ingin Anda beli dilengkapi dengan headphone stereo atau surround. Suara stereo (2.0) memiliki dua output, yang tidak menjamin kinerja audio yang sempurna. Di sisi lain, teknologi surround memiliki dua opsi: 5.1 dan 7.1, di mana angka pertama sesuai dengan jumlah saluran suara;",
            "Jika Anda menggunakan komputer di lingkungan yang sangat bising, Anda mungkin ingin mendapatkan headset peredam bising;",
            "Kekuatan headset inilah yang menentukan seberapa keras suara yang dihasilkan. Artinya, jika Anda ingin mendengar audio yang sangat keras, Anda harus mencari headphone dengan daya tertinggi. Headphone yang baik dimulai dengan daya 50 miliwatt. dan yang paling kuat bisa mencapai 150 miliwatt."
        ),
        arrayOf(
            "Pilih catu daya dengan sertifikasi 80 Plus; sertifikasi ini diberikan untuk catu daya dengan efisiensi (rasio antara daya yang dikonsumsi dan energi yang dipasok) lebih besar dari atau sama dengan 80%",
            "Ada 6 jenis sertifikasi 80 Plus: Standar (hasil 80%), Perunggu (hasil 82%), Perak (hasil 85%), Emas (hasil 87%), Platinum (hasil 90%) dan Titanium (hasil 94%) ). Oleh karena itu, catu daya 500W dengan sertifikasi Standar 80 Plus menghasilkan daya sekitar 400W.",
            "Verifikasi bahwa watt catu daya yang sebenarnya cukup untuk konfigurasi PC Anda;",
            "Hati-hati dengan faktor bentuk catu daya (Form Factor): ATX, Micro ATX, dan MINI ITX;",
            "Ada tiga jenis kabel catu daya: modular, di mana semua kabel tidak terhubung secara tetap ke catu daya, semi-modular, di mana beberapa kabel tetap dan yang lainnya tidak, dan non-modular, di mana semua kabel terpasang. terhubung secara tetap."
        ),
        arrayOf(
            "Ukuran dan bentuk casing ditentukan oleh faktor bentuk motherboard, yaitu ATX (Advanced Technology Extended), EATX (Extended ATX), Micro ATX, dan Mini ITX (Information Technology eXtended), yaitu faktorkan bentuk motherboard Anda yang menentukan casing seperti apa yang harus Anda beli;",
            "Lebih suka lemari dengan aliran udara yang baik, yaitu lemari yang memiliki saluran masuk dan keluar udara yang ditempatkan dengan baik dan yang mendukung satu atau lebih kipas;",
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_part_activity)
        var pos = intent.getIntExtra("id", 0)

        compName.text = names[pos]
        definition_text.text = definitions[pos]


        layoutManager = LinearLayoutManager(this)
        tips_list.layoutManager = layoutManager

        adapter = TipsRecyclerAdapter(tips[pos])
        tips_list.adapter = adapter


        imageView2.setImageResource(photos[pos])
        partList.setOnClickListener {
            val intent = Intent(this, ListProductActivity::class.java)
            intent.putExtra("part", key[pos])
            startActivity(intent)
        }
    }


}