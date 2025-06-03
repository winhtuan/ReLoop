from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import time

# Cấu hình trình duyệt Chrome
chrome_options = Options()
chrome_options.add_argument('--ignore-certificate-errors')
chrome_options.add_argument('--disable-blink-features=AutomationControlled')
chrome_options.add_argument('--headless')  # Tùy chọn chạy ẩn (nếu không cần hiển thị)

# Khởi tạo trình duyệt
driver = webdriver.Chrome(options=chrome_options)

# Truy cập trang web
url = "https://www.chotot.com/mua-ban-cho-da-nang"
driver.get(url)

# Đợi phần tử chính của danh sách sản phẩm xuất hiện
WebDriverWait(driver, 20).until(
    EC.presence_of_element_located((By.CLASS_NAME, "c14axl8t"))
)

# Lấy HTML sau khi trang đã tải xong
html = driver.page_source

# Đóng trình duyệt
driver.quit()

# Parse HTML với BeautifulSoup
soup = BeautifulSoup(html, "html.parser")

# Tìm các sản phẩm
products = soup.find_all("a", class_="c14axl8t")

# Duyệt qua từng sản phẩm để lấy dữ liệu
for product in products:
    try:
        # Hình ảnh
        img_tag = product.find('img')
        image_url = img_tag['src'] if img_tag else "Không có"

        # Tên
        title_tag = product.find('h3')
        title = title_tag.get_text(strip=True) if title_tag else "Không có"

        # Giá
        price_tag = product.find('span', class_='bfe6oav')
        price = price_tag.get_text(strip=True) if price_tag else "Không có"

        # Vị trí
        location_tag = product.find('span', string=lambda x: x and 'Quận' in x)
        location = location_tag.get_text(strip=True) if location_tag else "Không có"

        # Kết quả
        print("Hình ảnh:", image_url)
        print("Tên:", title)
        print("Giá:", price)
        print("Vị trí:", location)
        print("----------")
    except Exception as e:
        print("Lỗi khi xử lý một sản phẩm:", e)
        print("----------")
