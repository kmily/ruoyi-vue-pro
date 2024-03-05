import request from '@/utils/request'

// 创建悠悠商品列表
export function createYouyouCommodity(data) {
  return request({
    url: '/steam/youyou-commodity/create',
    method: 'post',
    data: data
  })
}

// 更新悠悠商品列表
export function updateYouyouCommodity(data) {
  return request({
    url: '/steam/youyou-commodity/update',
    method: 'put',
    data: data
  })
}

// 删除悠悠商品列表
export function deleteYouyouCommodity(id) {
  return request({
    url: '/steam/youyou-commodity/delete?id=' + id,
    method: 'delete'
  })
}

// 获得悠悠商品列表
export function getYouyouCommodity(id) {
  return request({
    url: '/steam/youyou-commodity/get?id=' + id,
    method: 'get'
  })
}

// 获得悠悠商品列表分页
export function getYouyouCommodityPage(params) {
  return request({
    url: '/steam/youyou-commodity/page',
    method: 'get',
    params
  })
}
// 导出悠悠商品列表 Excel
export function exportYouyouCommodityExcel(params) {
  return request({
    url: '/steam/youyou-commodity/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}