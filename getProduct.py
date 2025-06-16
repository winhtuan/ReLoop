from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import time

# 1️⃣ Cấu hình Chrome
chrome_options = Options()
chrome_options.add_argument('--ignore-certificate-errors')
chrome_options.add_argument('--disable-blink-features=AutomationControlled')
chrome_options.add_argument('--headless')
chrome_options.add_argument('--window-size=1920,1080')
chrome_options.add_argument('--disable-gpu')
chrome_options.add_argument('--no-sandbox')
chrome_options.add_argument('--disable-extensions')

# 2️⃣ Khởi tạo trình duyệt
driver = webdriver.Chrome(service=Service(), options=chrome_options)

try:
    # 3️⃣ Truy cập trang web
    url = "https://www.chotot.com/mua-ban-may-anh-may-quay-da-nang"#thay duong dan cho nay
    driver.get(url)

    # 4️⃣ Chờ danh sách sản phẩm xuất hiện
    WebDriverWait(driver, 20).until(
        EC.presence_of_element_located((By.CSS_SELECTOR, "a[href*='/mua-ban-may-anh-may-quay']"))#thay cho nay
    )

    # 5️⃣ Cuộn nhỏ từng bước để ép trigger lazy-loading
    page_height = driver.execute_script("return document.body.scrollHeight")
    for y in range(0, page_height, 300):
        driver.execute_script(f"window.scrollTo(0, {y});")
        time.sleep(0.3)

    # 6️⃣ Ép load toàn bộ ảnh lazy-loading
    driver.execute_script("""
        window.IntersectionObserver = undefined;
        document.querySelectorAll('img').forEach(img => {
            ['data-src', 'data-srcset', 'data-lazy-src'].forEach(attr => {
                if (img.hasAttribute(attr)) {
                    img.setAttribute('src', img.getAttribute(attr));
                    img.removeAttribute(attr);
                }
            });
            if (img.hasAttribute('data-srcset')) {
                img.setAttribute('srcset', img.getAttribute('data-srcset'));
                img.removeAttribute('data-srcset');
            }
            img.loading = 'eager';
            img.removeAttribute('lazy');
        });
    """)
    time.sleep(3)

    # 7️⃣ Lấy HTML đầy đủ
    html = driver.page_source

finally:
    # 8️⃣ Đóng trình duyệt
    driver.quit()

# 9️⃣ Phân tích HTML bằng BeautifulSoup
soup = BeautifulSoup(html, "html.parser")
product_links = soup.find_all("a", attrs={"itemprop": "item"})

# 🔟 Mở file để ghi dữ liệu
with open("D:\\ReLoop_Project\\product_sample.txt", "a", encoding="utf-8") as f:
    for product in product_links:
        try:
            img_tag = product.find('img')
            image_url = "Không có"
            if img_tag:
                image_url = img_tag.get('src') or img_tag.get('srcset') or "Không có"
                if ',' in image_url:
                    image_url = image_url.split(',')[0].strip().split(' ')[0]

            title_tag = product.find('h3')
            title = title_tag.get_text(strip=True) if title_tag else "Không có"

            price_tag = product.find('span', class_='bfe6oav')
            price = price_tag.get_text(strip=True) if price_tag else "Không có"

            location_tag = product.find('span', string=lambda x: x and 'Quận' in x)
            location = location_tag.get_text(strip=True) if location_tag else "Không có"

            # Ghi vào file
            f.write(f"Hình ảnh: {image_url}\n")
            f.write(f"Tên: {title}\n")
            f.write(f"Giá: {price}\n")
            f.write(f"Vị trí: {location}\n")
            f.write("----------\n")
        except Exception as e:
            f.write(f"Lỗi khi xử lý một sản phẩm: {e}\n")
            f.write("----------\n")
