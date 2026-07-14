export const buildFormData = (data) => {
    const fd = new FormData()
    Object.keys(data).forEach(key => {
        if (data[key] !== undefined && data[key] !== null) {
            fd.append(key, data[key])
        }
    })
    return fd
}